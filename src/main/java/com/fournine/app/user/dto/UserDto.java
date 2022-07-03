package com.fournine.app.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class UserDto {
	@ApiModelProperty(example = "27")
	private Integer userNo;
	@ApiModelProperty(example = "238903237")
	private String socialId;
	@ApiModelProperty(example = "kakao")
	private String socialType;
	@ApiModelProperty(example = "홍길동")
	private String userNickname;
	@ApiModelProperty(example = "수익을 공유하고 싶어요!")
	private String introduction;
	@ApiModelProperty(example = "images/profile/test.jpg")
	private String profileImagePath;
	@ApiModelProperty(example = "T")
	private String isPublic;
	@ApiModelProperty(example = "F")
	private String isDeleted;
}
