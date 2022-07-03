package com.fournine.framework.security;

import com.fournine.app.token.mapper.TokenMapper;
import com.fournine.app.user.mapper.UserMapper;
import com.fournine.framework.exception.FilterExceptionHandlers;
import com.fournine.framework.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fournine.app.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private TokenMapper tokenMapper;

	@Autowired
	private FilterExceptionHandlers filterExceptionHandlers;

	private static final String[] PUBLIC_MATCHERS = {
			"/", "/csrf",
			"/v3/api-docs/**", "/planning/", "/swagger-ui/**", "/swagger-ui.html",
			"/login/**",
			"/token/**",
			"/swagger-ui/index.html", "/swagger.json",
			"/swagger-resources/**", "/webjars/**",
			"/v2/api-docs", "/configuration/**", "/swagger*/**",
			"/staticfiles/**", "/static/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// 개발 편의성을 위해 CSRF 프로텍션을 비활성화
			.csrf().disable()
			// 리소스 별 허용 범위 설정  
			.authorizeRequests()
				.antMatchers(PUBLIC_MATCHERS).permitAll()
				.anyRequest().authenticated() // 인증한 사용자만 접근
			.and()
				// HTTP 기본 인증 비활성화
				.httpBasic().disable()
				//.formLogin().loginPage("/login")
			//.and()
				// 폼 기반 인증 비활성화  
				.formLogin().disable()
				// stateless한 세션 정책 설정  (Spring Security에서 Session을 생성하거나 사용하지 않도록 설정)
				// SecurityContextHolder 사용을 위해선 ALWAYS 로 변경해야 함
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				// 토큰 인증 필터 추가
				.addFilterBefore(new JWTAuthenticationFilter(jwtUserDetailsServiceImpl, jwtTokenUtil, tokenMapper), UsernamePasswordAuthenticationFilter.class)
				// 토큰 인증 필터 예외처리 핸들러 추가
				.addFilterBefore(filterExceptionHandlers, JWTAuthenticationFilter.class)
				// 인증 오류 발생 시 처리를 위한 핸들러 추가  
				.exceptionHandling()
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
					.accessDeniedHandler(jwtAccessDeniedHandler)
		;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(PUBLIC_MATCHERS);
	}
}
