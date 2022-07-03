package com.fournine.framework.exception;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.response.ResponseError;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {
	
	public void addErrorMsgToResponse(ResponseError response, HttpStatus statusCode, String message, Exception ex) {
		log.error(ex.getMessage());
		response.setStatusCode(statusCode)
				.setMessage(message) // Handler에서 전달한 메시지
				.setDetails(ex.getMessage()); // Exception 기본 메시지
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ServletRequestBindingException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final ServletRequestBindingException ex) {
		ResponseError response = new ResponseError();
		addErrorMsgToResponse(response, HttpStatus.BAD_REQUEST, "파라미터가 누락되었습니다.", (Exception) ex);
		log.error("파라미터가 누락되었습니다." + ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final IllegalArgumentException ex) {
		ResponseError response = new ResponseError();
		addErrorMsgToResponse(response, HttpStatus.BAD_REQUEST, "인자가 잘못되었습니다.", (Exception) ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final AccessDeniedException ex) {
		ResponseError response = new ResponseError();
		addErrorMsgToResponse(response, HttpStatus.FORBIDDEN, "접근권한이 없습니다.<br/>관리자에게 문의하세요.(포탈)", (Exception) ex);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final NotFoundException ex) {
		ResponseError response = new ResponseError();
		addErrorMsgToResponse(response, HttpStatus.NOT_FOUND, "Not Found", (Exception) ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final Throwable ex) {
		ResponseError response = new ResponseError();
		String message = ex.getMessage();
		if(ex instanceof NullPointerException) {
			message = "NullPointerException";
			ex.printStackTrace();
		}
		addErrorMsgToResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, message, (Exception) ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final AuthenticationException ex) {
		ResponseError response = new ResponseError();
		addErrorMsgToResponse(response, HttpStatus.UNAUTHORIZED, "인증 오류가 발생했습니다.", (Exception) ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final BadCredentialsException ex) {
		ResponseError response = new ResponseError();
		addErrorMsgToResponse(response, HttpStatus.UNAUTHORIZED, "인증 오류가 발생했습니다.", (Exception) ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	// 커스텀한 Exception
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> businessExceptionHandle(final BusinessException ex, HttpServletResponse res) throws IOException {
		ResponseError response = new ResponseError();
		HttpStatus statusCode = ex.getExceptionCode().getHttpStatus();
		String message = ex.getMessage();
		addErrorMsgToResponse(response, statusCode, message, (Exception) ex);
		return ResponseEntity.status(statusCode).body(response);
	}

	// [공통] RESTAPI 4XX 에러 처리 => HttpClientErrorException
	@ExceptionHandler(HttpClientErrorException.class)
	@ResponseBody
	public ResponseEntity<ResponseError> handle(final HttpClientErrorException ex, HttpServletResponse res) throws IOException {
		ResponseError response = new ResponseError();
		HttpStatus statusCode = ex.getStatusCode();

		String message = ex.getMessage();
		if(statusCode.equals(HttpStatus.CONFLICT)) {
			message = "해당 리소스가 이미 존재 합니다.";
		}else if(statusCode.equals(HttpStatus.NOT_FOUND)) {
			message = "해당 데이터를 찾을 수 없습니다.";
		}

		res.setStatus(statusCode.value());
		addErrorMsgToResponse(response, statusCode, message, (Exception) ex);
		return ResponseEntity.status(statusCode).body(response);
	}

}
