package com.fournine.app.token.controller;

import com.fournine.app.token.dto.TokenDto;
import com.fournine.app.token.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "토큰 관리")
@RestController
@RequestMapping("/token")
public class TokenController {
	@Autowired
	TokenService tokenService;

	@ApiOperation(value = "토큰 갱신하기")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "requestDto",
					value = "발급 받은 refresh Token을 넣습니다.",
					dataType = "PostReqTokenDto",
					paramType = "body",
					required = true)
	})
	@PostMapping(value = "")
	public ResponseEntity<TokenDto> reissueToken(@RequestBody TokenDto.PostReqTokenDto requestDto) throws Exception {
		return ResponseEntity.ok().body(tokenService.reissueToken(StringUtils.trim(requestDto.getRefreshToken())));
	}
}
