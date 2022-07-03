package com.fournine.framework.exception.business;

import lombok.Getter;

/*
 * 커스텀 Exception BASE
 */
@Getter
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

	private ExceptionCode exceptionCode;
	
	public BusinessException(String message, ExceptionCode exceptionCode) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	public BusinessException(ExceptionCode exceptionCode){
		super(exceptionCode.getMessage());
		this.exceptionCode = exceptionCode;
	}
}
