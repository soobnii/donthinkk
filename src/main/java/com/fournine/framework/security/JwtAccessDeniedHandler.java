package com.fournine.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		HashMap<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("statusCode", HttpStatus.FORBIDDEN);
		resMap.put("message", "인증에 실패했습니다.");
		resMap.put("details", accessDeniedException.getMessage());

		String json = new ObjectMapper().writeValueAsString(resMap);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");
		response.getWriter().write(json);
	}
}
