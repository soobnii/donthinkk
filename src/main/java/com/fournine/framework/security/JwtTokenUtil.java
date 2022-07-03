package com.fournine.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	@Value("${jwt.secret}")
	private String secret;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 약 5시간
	public static final long REFRESH_TOKEN_VALIDITY = 14 * 24 * 60 * 60; // 약 2주

//	public static final long JWT_TOKEN_VALIDITY = 60; // 약 1분
//	public static final long REFRESH_TOKEN_VALIDITY = 2 * 60; // 약 2분

	// jwt 토큰에서 사용자 이름 검색
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getId);
	}

	//  토큰에서 정보를 검색하기 위한 비밀 키
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	//  토큰에서 정보를 검색하기 위한 비밀 키
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	// 토큰이 만료되었는지 확인
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// jwt 토큰에서 만료 날짜 검색
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// 사용자를위한 토큰 생성
	public String generateToken(String id) {
		return generateToken(id, new HashMap<>());
	}

	// 사용자를위한 토큰 생성
	public String generateToken(String id, Map<String, Object> claims) {
		return doGenerateToken(id, claims);
	}
	
	// 사용자를위한 토큰 생성
	public String generateToken(UserDetails userDetails, String socialType) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("VALIDITY", JWT_TOKEN_VALIDITY);
		claims.put("SOCIALTYPE", socialType);
		return doGenerateToken(userDetails.getUsername(),claims);
	}

	public String generateRefreshToken(UserDetails userDetails, String socialType) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("VALIDITY", REFRESH_TOKEN_VALIDITY);
		claims.put("SOCIALTYPE", socialType);
		return doGenerateToken(userDetails.getUsername(), claims);
	}

	/* 토큰을 만드는 동안-
	  * 1. 발행자, 만료일, 제목 및 ID와 같은 토큰의 클레임을 정의합니다.
	  * 2. HS512 알고리즘과 비밀 키를 사용하여 JWT에 서명합니다.
	  * 3. JWS Compact에 따르면
	  * 직렬화 (https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	  * JWT를 URL 안전 문자열로 압축*/
	private String doGenerateToken(String id, Map<String, Object> claims) {
		return Jwts.builder()
				.setClaims(claims)
				.setId(id)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(String.valueOf(claims.get("VALIDITY"))) * 1000L))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	

	// 토큰 유효성 검사
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
}
