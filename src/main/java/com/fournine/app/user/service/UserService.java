package com.fournine.app.user.service;

import java.util.List;

import com.fournine.app.user.dto.UserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fournine.app.user.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

	public List<UserDto> getUsers() throws Exception;
	public UserDto getUser(int userNo) throws Exception;

	public boolean createUser(UserDto userdto) throws Exception;
	public UserDto updateUserInfo(UserDto userDto);
	public ResponseEntity<?> removeUserInfo(int userNo);
	public UserProfile updateUserProfile(int userNo, MultipartFile profileImageFile);
	public UserProfile initUserProfile(int userNo);
}
