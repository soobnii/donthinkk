package com.fournine.app.login.service;

import com.fournine.app.login.dto.LoginDto;
import com.fournine.app.user.dto.UserDto;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

public interface LoginService {
	public LoginDto loginBySocialAuth(String socialToken, String socialType) throws Exception;
	public ResponseEntity<?> logout(String refreshToken) throws Exception;
}
