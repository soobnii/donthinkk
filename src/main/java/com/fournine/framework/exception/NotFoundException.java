package com.fournine.framework.exception;

/*
 * 이미 존재하는 NotFoundException 을 커스텀
 */
@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
	
	public NotFoundException(String msg) {
		super(msg);
	}

	public NotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
