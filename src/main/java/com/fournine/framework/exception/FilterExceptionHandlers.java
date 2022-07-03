package com.fournine.framework.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.response.ResponseError;
import com.fournine.framework.util.CustomStringUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class FilterExceptionHandlers extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (BusinessException e) {
			setErrorResponse(response, e.getExceptionCode().getHttpStatus(), e.getExceptionCode().getMessage(), e);
		} catch (ExpiredJwtException | IllegalArgumentException e) {
			setErrorResponse(response, HttpStatus.FORBIDDEN, "서버에 있는 파일에 접근할 권한이 없습니다.", e);
		} catch (RuntimeException e) {
			setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "런타임 에러가 발생했습니다.", e);
		} catch (Exception e) {
			setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "예외가 발생했습니다.", e);
		}
	}

	// 응답 에러폼
	public void setErrorResponse(HttpServletResponse response, HttpStatus status, String message, Throwable ex){
		response.setStatus(status.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		ResponseError errorResponse = new ResponseError();
		errorResponse.setStatusCode(status)
					.setMessage(message)
					.setDetails(ex.getMessage());

		try{
			String jsonBody = CustomStringUtils.convertObjectToJson(errorResponse);
			response.getWriter().write(jsonBody);
		}catch (IOException e){
			log.error(e.getMessage());
		}
	}
}
