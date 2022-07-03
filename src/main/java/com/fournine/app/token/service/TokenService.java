package com.fournine.app.token.service;

import com.fournine.app.token.dto.TokenDto;

import javax.servlet.http.HttpServletRequest;


public interface TokenService {
	public TokenDto reissueToken(String refreshToken) throws Exception;
}
