package com.fournine.framework.security;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	//exceptionHandling - Controller 가 아닌 JWTAuthenticationFilter 에서 발생한 exception 처리
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {	
		
		HashMap<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("statusCode", HttpStatus.UNAUTHORIZED);
		resMap.put("message", "인증에 실패했습니다.");
		resMap.put("details", authException.getMessage());
		log.error("JwtFilter 인증 에러");
		log.error(authException.getMessage());
		
		String json = new ObjectMapper().writeValueAsString(resMap);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");
		response.getWriter().write(json);
	}
}
