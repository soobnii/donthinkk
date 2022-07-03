package com.fournine.app.user.service.impl;

import com.fournine.app.user.dto.UserDto;
import com.fournine.app.user.dto.UserProfile;
import com.fournine.app.user.jpa.entity.UserEntity;
import com.fournine.app.user.jpa.repository.UserRepository;
import com.fournine.app.user.mapper.UserMapper;
import com.fournine.app.user.service.UserService;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.exception.business.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserMapper userMapper;

	@Value("${property.images.profile-path}")
	private String imagesProfilePath;

	@Override
	public List<UserDto> getUsers() throws Exception {
		/*List<UserEntity> userEntityList = userRepository.findAll();
		List<UserDto> users = new ArrayList<UserDto>();
		userEntityList.forEach(entity -> {
			users.add(modelMapper.map(entity, UserDto.class));
		});*/
		List<UserDto> users = userMapper.getAllUserInfo();

		return users;
	}

	@Override
	public UserDto getUser(int userNo) {
		UserDto user = userMapper.getUserSearch(userNo);
		if (ObjectUtils.isEmpty(user)) {
			throw new BusinessException("해당 사용자는 없는 사용자입니다.", ExceptionCode.USER_NOT_FOUND);
		}
		return user;
	}

	@Override
	public boolean createUser(UserDto userDto) throws Exception {
		int insertResult = userMapper.insertUserInfo(userDto);

		if (insertResult < 1) {
			return false;
		}

		return true;
	}

	@Override
	public UserDto updateUserInfo(UserDto userDto) {
		int ret = userMapper.updateUserInfoByUserNo(userDto);
		if (ret == 0) {
			throw new BusinessException("사용자 정보 수정에 실패했습니다.", ExceptionCode.USER_NOT_FOUND);
		}

		return userDto;
	}

	@Override
	public ResponseEntity<?> removeUserInfo(int userNo) {
		userMapper.deleteUserInfo(userNo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public UserProfile updateUserProfile(int userNo, MultipartFile profileImageFile) {
		String imagePath = null;
		// 윈도우 로컬에서 테스트할 때 사용
		//String resourcePath = new File("").getAbsolutePath() + "\\";
		//String path = "images/profile";
		File file = new File(this.imagesProfilePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String originalFileExtension = "";
		if (!profileImageFile.isEmpty()) {
			String contentType = profileImageFile.getContentType();
			if (ObjectUtils.isEmpty(contentType)) {
				throw new BusinessException("이미지 파일은 jpg, png 만 가능합니다.", ExceptionCode.FILE_BAD_REQUEST);
			} else {
				if (contentType.contains("image/jpeg")) {
					originalFileExtension = ".jpg";
				} else if (contentType.contains("image/png")) {
					originalFileExtension = ".png";
				} else {
					throw new BusinessException("이미지 파일은 jpg, png 만 가능합니다.", ExceptionCode.FILE_BAD_REQUEST);
				}
			}
			// 윈도우 로컬에서 테스트할 때 사용.
			/*imagePath = path + "/" + userNo + "_profile" + originalFileExtension;
			file = new File(resourcePath + imagePath);*/
			imagePath = "/" + userNo + "_profile" + originalFileExtension;
			file = new File(this.imagesProfilePath + imagePath);
			try {
				profileImageFile.transferTo(file);
			} catch (IOException e) {
				throw new BusinessException(ExceptionCode.FILE_UPLOAD_ERROR);
			}
		} else {
			throw new BusinessException("이미지 파일이 비어있습니다.", ExceptionCode.FILE_BAD_REQUEST);
		}

		String profilePath = "images/profile";
		userMapper.updateUserProfilePath(userNo, profilePath + imagePath);

		UserProfile userProfile = new UserProfile();
		userProfile.setUserNo(userNo)
				.setProfileImagePath(profilePath + imagePath);

		return userProfile;
	}

	@Override
	public UserProfile initUserProfile(int userNo) {
		userMapper.updateUserProfilePath(userNo, null);

		UserProfile userProfile = new UserProfile();
		userProfile.setUserNo(userNo)
				.setProfileImagePath(null);

		return userProfile;
	}
}
