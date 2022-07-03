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
public class UserProfile {
	@ApiModelProperty(example = "27")
	private Integer userNo;
	@ApiModelProperty(example = "/images/profile/test.jpg")
	private String profileImagePath;
}
