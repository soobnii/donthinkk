package com.fournine.framework.security;

import com.fournine.app.login.dto.LoginDto;
import com.fournine.app.login.mapper.LoginMapper;
import com.fournine.app.user.dto.UserDto;
import com.fournine.app.user.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(MockitoJUnitRunner.class) Mock 데이터 사용할때 쓰기
@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenUtilTest {
//	@InjectMocks
	@Autowired
	JwtUserDetailsServiceImpl jwtUserDetailService;
	@Autowired
	JwtTokenUtil jwtTokenUtil;

//	@Mock
	@Autowired
	UserMapper userMapper;
	@Autowired
	LoginMapper loginMapper;

	@Test
	public void doSomething() throws Exception {
		JwtUserDetailsServiceImpl jwtUserDetailsService = new JwtUserDetailsServiceImpl();
		UserDto userInfoDto = new UserDto();
		userInfoDto.setSocialId("2175840370").setSocialType("kakao");

		UserDetails userDetails = jwtUserDetailService.loadUserBySocialId(userInfoDto.getSocialId(), userInfoDto.getSocialType());
		UserDto userDto = userMapper.getUserBySocialId(userInfoDto.getSocialId(), userInfoDto.getSocialType());

//		LoginDto loginDto = new LoginDto();
//		loginDto.setSocialId("2175840377").setSocialType("apple").setRefreshToken("refreshTokenTest");
//		loginMapper.insertTokenInfo(loginDto);
//		assertEquals(20, userInfoDto);
		String token = jwtTokenUtil.generateToken(userDetails, userInfoDto.getSocialType());
		assertThat(token).isEqualTo(1);
	}
}