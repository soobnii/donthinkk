package com.fournine.app.user.controller;

import com.fournine.app.login.service.LoginService;
import com.fournine.app.token.mapper.TokenMapper;
import com.fournine.app.user.dto.UserDto;
import com.fournine.app.user.dto.UserProfile;
import com.fournine.app.user.service.UserService;
import com.fournine.framework.security.JwtTokenUtil;
import com.fournine.framework.security.JwtUserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(value = "사용자 관리")
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	LoginService loginService;

	@Autowired
	JwtUserDetailsServiceImpl jwtUserDetailsService;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	TokenMapper tokenMapper;

	@ApiOperation(value = "사용자 목록 조회")
	@GetMapping(value = "")
	public ResponseEntity<List<UserDto>> getUsers() throws Exception {
		return ResponseEntity.ok(userService.getUsers());
	}

	@ApiOperation(value = "특정 사용자 정보 조회")
	@GetMapping(value = "/{userNo}")
	public ResponseEntity<UserDto> getUser(@PathVariable("userNo") int userNo) throws Exception {
		return ResponseEntity.ok(userService.getUser(userNo));
	}

	@ApiOperation(value = "사용자 생성")
	@PostMapping(value = "")
	public ResponseEntity<?> joinUser(@RequestBody UserDto userDto) throws Exception {
		userService.createUser(userDto);
		return ResponseEntity.ok("");
	}

	@ApiOperation(value = "사용자 정보 수정")
	@PutMapping(value = "/{userNo}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("userNo") int userNo,
										@RequestBody UserDto requestDto) {
		requestDto.setUserNo(userNo);
		return ResponseEntity.ok(userService.updateUserInfo(requestDto));
	}

	@ApiOperation(value = "사용자 정보 삭제")
	@DeleteMapping(value = "/{userNo}")
	public ResponseEntity<?> removeUser(@PathVariable("userNo") int userNo) {
		return ResponseEntity.ok(userService.removeUserInfo(userNo));
	}

	@ApiOperation(value = "사용자 프로필 이미지 업로드")
	@PostMapping(value = "/profile/{userNo}")
	public ResponseEntity<UserProfile> updateUserProfile(@PathVariable("userNo") int userNo, @RequestBody MultipartFile profileImageFile) {
		return ResponseEntity.ok(userService.updateUserProfile(userNo, profileImageFile));
	}

	@ApiOperation(value = "사용자 프로필 이미지 초기화")
	@PutMapping(value = "/profile/{userNo}")
	public ResponseEntity<UserProfile> initUserProfile(@PathVariable("userNo") int userNo) {
		return ResponseEntity.ok(userService.initUserProfile(userNo));
	}
}
