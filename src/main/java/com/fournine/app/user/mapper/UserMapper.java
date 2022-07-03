package com.fournine.app.user.mapper;

import com.fournine.app.user.dto.UserDto;
import com.fournine.app.user.dto.UserProfile;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@MapperScan
@Repository
public interface UserMapper {

	public List<UserDto> getAllUserInfo();
	public UserDto getUserSearch(int userNo);
	public UserDto getUserBySocialId(String socialId, String socialType) throws Exception;

	public UserDto getUserInfo(String socialId, String socialType);

	public int insertUserInfo(UserDto userDto) throws Exception;

	public int insertUserForSignup(UserDto userInfoDto);
	public int updateUserInfoByUserNo(UserDto userDto);
	public int deleteUserInfo(int userNo);
	public int updateUserProfilePath(int userNo, String profileImagePath);
}
