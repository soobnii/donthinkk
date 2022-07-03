package com.fournine.app.token.service.impl;

import com.fournine.app.token.dto.TokenDto;
import com.fournine.app.token.mapper.TokenMapper;
import com.fournine.app.token.service.TokenService;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.exception.business.ExceptionCode;
import com.fournine.framework.security.JwtTokenUtil;
import com.fournine.framework.security.JwtUserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	TokenMapper tokenMapper;
	@Autowired
	JwtUserDetailsServiceImpl jwtUserDetailsService;

	@Override
	public TokenDto reissueToken(String refreshToken) throws Exception {

		if (StringUtils.isEmpty(refreshToken)) {
			throw new BusinessException("리프레시 토큰이 없습니다.", ExceptionCode.REQUEST_PARAMS_INVALID);
		}

		TokenDto tokenInfo = new TokenDto();

		try {
			Claims refreshTokenInfo = jwtTokenUtil.getAllClaimsFromToken(refreshToken);
			String socialId = refreshTokenInfo.get("jti").toString();
			String socialType = refreshTokenInfo.get("SOCIALTYPE").toString();
			UserDetails userDetails = jwtUserDetailsService.loadUserBySocialId(socialId, socialType);

			if (!jwtTokenUtil.validateToken(refreshToken, userDetails)) {
				throw new BusinessException("유효하지 않은 리프레시 토큰입니다.", ExceptionCode.TOKEN_BAD_REQUEST);
			}

			String dbRefreshToken = tokenMapper.getRefreshToken(socialId, socialType);
			if (StringUtils.isEmpty(dbRefreshToken) || !dbRefreshToken.equals(refreshToken)) {
				throw new BusinessException(ExceptionCode.TOKEN_BAD_REQUEST);
			}

			String newAccessToken = jwtTokenUtil.generateToken(userDetails, socialType);
			String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails, socialType);

			tokenMapper.updateRefreshToken(socialId, socialType, newRefreshToken);

			tokenInfo.setAccessToken(newAccessToken)
					.setAccessTokenExpiration(jwtTokenUtil.getExpirationDateFromToken(newAccessToken))
					.setRefreshToken(newRefreshToken)
					.setRefreshTokenExpiration(jwtTokenUtil.getExpirationDateFromToken(newRefreshToken));

		} catch (ExpiredJwtException e) {
			// refresToken 만료
			log.error("ExpiredJwtException ::::: expiried JWT Refresh Token");
			throw new BusinessException("refresh 토큰 만료", ExceptionCode.TOKEN_EXPIRATION_DATE);
		} catch (Exception e) {
			log.error("ERROR ::: " + e.getMessage());
			throw new Exception(e.getMessage());
		}

		return tokenInfo;
	}
}
