package com.fournine.app.login.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fournine.app.token.dto.TokenDto;
import com.fournine.app.user.dto.UserDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;

//@NoArgsConstructor
//@Accessors(chain = true)
//@ToString
//@Getter
//@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)

/**
 * 깔끔하게 DTO 관리하기
 * https://velog.io/@ausg/Spring-Boot%EC%97%90%EC%84%9C-%EA%B9%94%EB%81%94%ED%95%98%EA%B2%8C-DTO-%EA%B4%80%EB%A6%AC%ED%95%98%EA%B8%B0
 * https://song8420.tistory.com/m/383
 */
@Accessors(chain = true)
@Getter
@Setter
public class LoginDto {
	private UserDto userInfo;
	private TokenDto tokenInfo;
	@ApiModelProperty(example = "T")
	private String isRegistered;

	@Getter
	public static class PostReqLoginDto {
		@ApiModelProperty(example = "fjteckpS3bR_ZcdRbcceGWlfdMEwo9c-sAAAGAA-ZCXg")
		private String socialToken;
		@ApiModelProperty(example = "kakao")
		private String socialType;
	}

	@Getter
	public static class DeleteReqLoginDto {
		@ApiModelProperty(example = "fjteckpS3bR_ZcdRbcceGWlfdMEwo9c-sAAAGAA-ZCXg")
		private String refreshToken;
	}
}
