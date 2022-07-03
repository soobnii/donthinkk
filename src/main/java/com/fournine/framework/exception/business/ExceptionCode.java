package com.fournine.framework.exception.business;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
	REQUEST_PARAMS_INVALID(HttpStatus.BAD_REQUEST, "요청 파라미터가 유효하지 않습니다."),
	SOCIAL_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "소셜 인증에 실패했습니다."),
	SOCIAL_DATA_INVALID(HttpStatus.FORBIDDEN, "소셜 정보가 유효하지 않습니다."),

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.(%s)"),
	USER_CREATE_FAIL(HttpStatus.NOT_FOUND, "사용자 생성에 실패했습니다."),
	TOKEN_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
	TOKEN_BAD_REQUEST(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
	TOKEN_EXPIRATION_DATE(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
	REVENUE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 수익정보를 찾을 수 없습니다.(%s)"),
	FAILED_TO_FIND_AVALIABLE_RSA(HttpStatus.UNAUTHORIZED, "사용할 수 있는 공개키를 찾지 못했습니다."),
	FILE_BAD_REQUEST(HttpStatus.NOT_FOUND, "파일이 유효하지 않습니다."),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을수 없습니다."),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드가 정상적으로 되지 않았습니다.");


	private final HttpStatus httpStatus;
	private final String message;

}
