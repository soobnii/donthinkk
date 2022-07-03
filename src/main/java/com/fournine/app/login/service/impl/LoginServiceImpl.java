package com.fournine.app.login.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fournine.app.login.dto.LoginDto;
import com.fournine.app.login.service.LoginService;
import com.fournine.app.token.dto.TokenDto;
import com.fournine.app.token.mapper.TokenMapper;
import com.fournine.app.user.dto.UserDto;
import com.fournine.app.user.mapper.UserMapper;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.exception.business.ExceptionCode;
import com.fournine.framework.security.JwtTokenUtil;
import com.fournine.framework.security.JwtUserDetailsServiceImpl;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
	private final String kakaoApiUrl = "https://kapi.kakao.com/v2/user/me";

	private final String appleApiRul = "https://appleid.apple.com/auth/keys";
	private final RestTemplate restTemplate;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	JwtUserDetailsServiceImpl jwtUserDetailsService;
	@Autowired
	UserMapper userMapper;
	@Autowired
	TokenMapper tokenMapper;

	public LoginDto loginBySocialAuth(String socialToken, String socialType){
		UserDto userInfo;
		if ("kakao".equals(socialType)) {
			// 카카오 사용자 데이터 가져오기
			userInfo = this.getUserFromKakao(socialToken, socialType);
		} else if ("apple".equals(socialType)) {
			userInfo = this.getUserFromApple(socialToken, socialType);
		} else {
			throw new BusinessException("소셜 타입이 올바르지 않습니다.", ExceptionCode.SOCIAL_DATA_INVALID);
		}

		LoginDto loginDto = new LoginDto();
		// DB에 등록된 사용자인지 체크한 후 데이터 삽입.
		if (ObjectUtils.isEmpty(userMapper.getUserInfo(userInfo.getSocialId(), userInfo.getSocialType()))) {
			String userNickname = StringUtils.isEmpty(userInfo.getUserNickname()) ? "익명" : userInfo.getUserNickname();
			userInfo.setUserNickname(userNickname);
			userMapper.insertUserForSignup(userInfo);
			// 로그인 안했던 사용자.
			loginDto.setIsRegistered("F");
		} else {
			// 로그인 했던 사용자.
			loginDto.setIsRegistered("T");
		}
		loginDto.setUserInfo(userMapper.getUserInfo(userInfo.getSocialId(), userInfo.getSocialType()));

		UserDetails userDetails;
		try {
			userDetails = jwtUserDetailsService.loadUserBySocialId(userInfo.getSocialId(), userInfo.getSocialType());
		} catch (Exception e) {
			throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
		}

		String accessToken = "";
		String refreshToken = "";
		Date accessTokenExpiration;
		Date refreshTokenExpiration;

		accessToken = jwtTokenUtil.generateToken(userDetails, userInfo.getSocialType());
		refreshToken = jwtTokenUtil.generateRefreshToken(userDetails, userInfo.getSocialType());
		accessTokenExpiration = jwtTokenUtil.getExpirationDateFromToken(accessToken);
		refreshTokenExpiration = jwtTokenUtil.getExpirationDateFromToken(refreshToken);

		String dbRefreshToken = tokenMapper.getTokenByUserInfo(userInfo.getSocialId(), userInfo.getSocialType());

		if (!StringUtils.isEmpty(dbRefreshToken)) {
			tokenMapper.updateRefreshToken(userInfo.getSocialId(), userInfo.getSocialType(), refreshToken);
		} else {
			tokenMapper.insertTokenInfo(userInfo.getSocialId(), userInfo.getSocialType(), refreshToken);
		}

		TokenDto tokenDto = new TokenDto();
		tokenDto.setAccessToken(accessToken)
				.setRefreshToken(refreshToken)
				.setAccessTokenExpiration(accessTokenExpiration)
				.setRefreshTokenExpiration(refreshTokenExpiration);

		loginDto.setTokenInfo(tokenDto);

		return loginDto;
	}

	private UserDto getUserFromKakao(String socialAccessToken, String socialType) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type", "application/json;charset=utf-8");
		headers.set("Authorization", "Bearer " + socialAccessToken);
		HttpEntity request = new HttpEntity(headers);

		ResponseEntity<JSONObject> response;
		try {
			response = restTemplate.exchange(this.kakaoApiUrl, HttpMethod.POST, request, JSONObject.class);
		} catch (Exception e) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.");
		}

		JSONObject resBody = response.getBody();

		if (ObjectUtils.isEmpty(resBody)) {
			throw new BusinessException(ExceptionCode.SOCIAL_UNAUTHORIZED);
		}
		JsonParser jsonParser = new JsonParser();
		JsonObject kakaoUser = (JsonObject) jsonParser.parse(resBody.toString());

		UserDto userDto = new UserDto();
		userDto.setSocialId(kakaoUser.get("id").getAsString())
				.setUserNickname(kakaoUser.getAsJsonObject("kakao_account").getAsJsonObject("profile").get("nickname").getAsString())
				.setSocialType("kakao");

		return userDto;
	}

	/**
	 * 1. apple로 부터 공개키 3개 가져옴
	 * 2. 클라이언트에서 가져온 token String과 비교해서 써야할 공개키 확인 (kid,alg 값 같은 것)
	 * 3. 그 공개키 재료들로 공개키 만들고, 이 공개키로 JWT토큰 부분의 바디 부분의 decode하면 유저 정보
	 */
	private UserDto getUserFromApple(String idToken, String socialType) {

		StringBuffer result = new StringBuffer();

		try {
			URL url = new URL(this.appleApiRul);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";

			while ((line = br.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			throw new BusinessException(ExceptionCode.SOCIAL_UNAUTHORIZED);
		}

		JsonParser parser = new JsonParser();
		JsonObject keys = (JsonObject) parser.parse(result.toString());
		JsonArray keyArray = (JsonArray) keys.get("keys");

		// 클라이언트로부터 가져온 identity token String decode
		String[] decodeArray = idToken.split("\\.");
		String header = new String(Base64.getDecoder().decode(decodeArray[0]));

		//apple에서 제공해주는 kid값과 일치하는지 알기 위해
		JsonElement kid = ((JsonObject) parser.parse(header)).get("kid");
		JsonElement alg = ((JsonObject) parser.parse(header)).get("alg");

		//써야하는 Element (kid, alg 일치하는 element)
		JsonObject avaliableObject = null;
		for (int i = 0; i < keyArray.size(); i++) {
			JsonObject appleObject = (JsonObject) keyArray.get(i);
			JsonElement appleKid = appleObject.get("kid");
			JsonElement appleAlg = appleObject.get("alg");

			if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
				avaliableObject = appleObject;
				break;
			}
		}

		//일치하는 공개키 없음
		if (ObjectUtils.isEmpty(avaliableObject))
			throw new BusinessException("공개키가 일치하지 않습니다.", ExceptionCode.FAILED_TO_FIND_AVALIABLE_RSA);

		PublicKey publicKey = this.getPublicKey(avaliableObject);

		//--> 여기까지 검증

		Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(idToken).getBody();
		JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));
		JsonElement appleAlg = userInfoObject.get("sub");

		UserDto userDto = new UserDto();
		userDto.setSocialId(appleAlg.getAsString())
				.setSocialType("apple");

		return userDto;
	}

	private PublicKey getPublicKey(JsonObject object) {
		String nStr = object.get("n").toString();
		String eStr = object.get("e").toString();

		byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
		byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

		BigInteger n = new BigInteger(1, nBytes);
		BigInteger e = new BigInteger(1, eBytes);

		try {
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			return publicKey;
		} catch (Exception exception) {
			throw new BusinessException(ExceptionCode.FAILED_TO_FIND_AVALIABLE_RSA);
		}
	}

	public ResponseEntity<?> logout(String refreshToken){
		int result = 0;
		if (StringUtils.isEmpty(refreshToken)) {
			throw new BusinessException("refreshToken이 없습니다.", ExceptionCode.REQUEST_PARAMS_INVALID);
		}

		try {
			Claims refreshTokenInfo = jwtTokenUtil.getAllClaimsFromToken(refreshToken);
			String socialId = refreshTokenInfo.get("jti").toString();
			String socialType = refreshTokenInfo.get("SOCIALTYPE").toString();
			String dbRefreshToken = tokenMapper.getRefreshToken(socialId, socialType);

			if (StringUtils.isEmpty(dbRefreshToken) || !dbRefreshToken.equals(refreshToken)) {
				throw new BusinessException("유효하지 않은 리프레시 토큰입니다.", ExceptionCode.TOKEN_BAD_REQUEST);
			}

			result = tokenMapper.deleteRefreshToken(socialId, socialType);
		} catch (ExpiredJwtException e) {
			// refresToken 만료
			log.error("ExpiredJwtException ::::: expiried JWT Refresh Token");
			throw new BusinessException("refresh 토큰 만료", ExceptionCode.TOKEN_EXPIRATION_DATE);
		} catch (Exception e) {
			log.error("ERROR ::: " + e.getMessage());
			throw new BusinessException(e.getMessage(), ExceptionCode.TOKEN_BAD_REQUEST);
		}

		if (result == 0) {
			throw new BusinessException(ExceptionCode.TOKEN_NOT_FOUND);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
