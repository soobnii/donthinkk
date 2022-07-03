package com.fournine.framework.security;

import com.fournine.app.token.mapper.TokenMapper;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.exception.business.ExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {


	private JwtUserDetailsServiceImpl jwtUserDetailsService;

	private JwtTokenUtil jwtTokenUtil;

	private TokenMapper tokenMapper;

	// JWT Token의 prefix는 Bearer
	public static final String tokenPrefix = "Bearer ";

	// JWT Token은 Authorization Header로 전달
	public static final String authHeader = "Authorization";

	// RefreshToken Header
	public static final String refreshHeader = "X-Refresh-Token";

	public JWTAuthenticationFilter(JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl, JwtTokenUtil jwtTokenUtil, TokenMapper tokenMapper) {
		this.jwtUserDetailsService = jwtUserDetailsServiceImpl;
		this.jwtTokenUtil = jwtTokenUtil;
		this.tokenMapper = tokenMapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, BusinessException {
		String headerAccessToken = request.getHeader(authHeader);
		String accessToken = null;
		String refreshToken = null;
		String newAccessToken = null; // 프론트에 응답해줄 토큰

		String socialId = null;
		String socialType = null;

		if (headerAccessToken.startsWith(tokenPrefix)) {
			// 엑세스토큰 검증
			try {
				accessToken = StringUtils.trim(headerAccessToken.replace(tokenPrefix, ""));
				Claims accessTokenInfo = jwtTokenUtil.getAllClaimsFromToken(accessToken);
				socialId = accessTokenInfo.get("jti").toString();
				socialType = accessTokenInfo.get("SOCIALTYPE").toString();

				UserDetails userDetails = jwtUserDetailsService.loadUserBySocialId(socialId, socialType);
				if (jwtTokenUtil.validateToken(accessToken, userDetails) == false) {
					throw new BusinessException(ExceptionCode.TOKEN_EXPIRATION_DATE);
				}

				// 아이디와 권한으로, Security 가 알아 볼 수 있는 token 객체로 변경한다.
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				// 실제 SecurityContext 에 authentication 정보를 등록한다.
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} catch (ExpiredJwtException e) {
				log.error("ExpiredJwtException ::::: expiried JWT Access Token");
				if (!StringUtils.isEmpty(request.getHeader(refreshHeader)) && request.getHeader(refreshHeader).startsWith(tokenPrefix)) {
					refreshToken = StringUtils.trim(request.getHeader(refreshHeader));
				}

			} catch (IllegalArgumentException e) {
				log.error("IllegalArgumentException ::::: Unable to get JWT Token");
				throw new IllegalArgumentException("토큰을 가져오는데 실패하였습니다.");
			} catch (Exception e) {
				log.error("ERROR ::: " + e.getMessage());
				throw new BusinessException(e.getMessage(), ExceptionCode.TOKEN_BAD_REQUEST);
			}
		}

		// 리프레시 토큰 체크해서 새로운 엑세스토큰 발급
		if (!StringUtils.isEmpty(refreshToken)) {
			try {
				Claims refreshTokenInfo = jwtTokenUtil.getAllClaimsFromToken(refreshToken);
				socialId = refreshTokenInfo.get("jti").toString();
				socialType = refreshTokenInfo.get("SOCIALTYPE").toString();
				String dbRefreshToken = tokenMapper.getRefreshToken(socialId, socialType);

				if (StringUtils.isEmpty(dbRefreshToken) || !dbRefreshToken.equals(refreshToken)) {
					throw new BusinessException("유효하지 않은 리프레시 토큰입니다.", ExceptionCode.TOKEN_BAD_REQUEST);
				}

				UserDetails userDetails = jwtUserDetailsService.loadUserBySocialId(socialId, socialType);
				if (jwtTokenUtil.validateToken(refreshToken, userDetails) == true) {
					newAccessToken = jwtTokenUtil.generateToken(userDetails, socialType);

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (ExpiredJwtException e) {
				// refresToken 만료
				log.error("ExpiredJwtException ::::: expiried JWT Refresh Token");
				throw new BusinessException("refresh 토큰 만료", ExceptionCode.TOKEN_EXPIRATION_DATE);
			} catch (Exception e) {
				log.error("ERROR ::: " + e.getMessage());
				throw new BusinessException(e.getMessage(), ExceptionCode.TOKEN_BAD_REQUEST);
			}
		}

		response.addHeader("X-Access-Token", newAccessToken);
		filterChain.doFilter(request, response);
	}

	public static String readBody(HttpServletRequest request) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String buffer;
		while ((buffer = input.readLine()) != null) {
			if (builder.length() > 0) {
				builder.append("\n");
			}
			builder.append(buffer);
		}
		return builder.toString();
	}
}
