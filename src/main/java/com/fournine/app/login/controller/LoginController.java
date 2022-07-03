package com.fournine.app.login.controller;

import com.fournine.app.login.dto.LoginDto;
import com.fournine.app.login.service.LoginService;
import com.fournine.app.token.mapper.TokenMapper;
import com.fournine.app.user.dto.UserDto;
import com.fournine.app.user.mapper.UserMapper;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.exception.business.ExceptionCode;
import com.fournine.framework.response.ResponseError;
import com.fournine.framework.security.JwtTokenUtil;
import com.fournine.framework.security.JwtUserDetailsServiceImpl;
import io.swagger.annotations.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Api(value = "로그인 처리")
@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	LoginService loginService;

	@ApiOperation(value = "로그인 - 소셜", notes = "카카오, 애플 Access token을 통해 fournine_app에 로그인한다.")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "requestDto", value = "Json type 사용", dataType = "PostReqLoginDto", paramType = "body", required = true)
	})
	@PostMapping(value = "")
	public ResponseEntity<LoginDto> login(@RequestBody LoginDto.PostReqLoginDto requestDto) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(loginService.loginBySocialAuth(requestDto.getSocialToken(), requestDto.getSocialType()));
	}

	@DeleteMapping(value = "")
	public ResponseEntity<?> logout(@RequestBody LoginDto.DeleteReqLoginDto requestDto) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(loginService.logout(requestDto.getRefreshToken()));
	}
}

