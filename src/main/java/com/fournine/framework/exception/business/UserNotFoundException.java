package com.fournine.framework.exception.business;

/*
 * 커스텀 Exception 확장
 * 경우에 따라 BusinessException 대신 RuntimeException 을 상속받기도 함
 */
public class UserNotFoundException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String userId) {
		// new UserNotFoundException("userId=" + userId)); 로 발생시킬 수 있음
		super(String.format(ExceptionCode.USER_NOT_FOUND.getMessage(), userId), ExceptionCode.USER_NOT_FOUND);
	}
	
}
