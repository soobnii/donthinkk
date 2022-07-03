package com.fournine.framework.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fournine.app.user.dto.UserDto;
import com.fournine.app.user.jpa.entity.UserEntity;
import com.fournine.app.user.mapper.UserMapper;
import com.fournine.app.user.service.UserService;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.exception.business.ExceptionCode;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	// userId, socialType으로 회원을 찾아서 결과적으로 User 객체를 반환하는 역할
	public UserDetails loadUserBySocialId(String socialId, String socialType) throws Exception {
		UserDto userDto = userMapper.getUserBySocialId(socialId, socialType);

		if(ObjectUtils.isEmpty(userDto) == true) {
			throw new BusinessException("인증된 사용자가 아닙니다.", ExceptionCode.TOKEN_USER_NOT_FOUND);
		}
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("USER"));

		//UserDetails details = new User(userDto.getSocialId(), userDto.getSocialType(), grantedAuthorities);
		UserEntity entity = new UserEntity();
		entity.setSocialId(userDto.getSocialId());
		entity.setSocialType(userDto.getSocialType());
		entity.setUserNo(userDto.getUserNo());
		
		return entity;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

//	public Member authenticateByEmailAndPassword(String email, String password) {
//		Member member = memberRepository.findByEmail(email)
//				.orElseThrow(() -> new UsernameNotFoundException(email));
//
//		if(!passwordEncoder.matches(password, member.getPassword())) {
//			throw new BadCredentialsException("Password not matched");
//		}
//
//		return member;
//	}

}
