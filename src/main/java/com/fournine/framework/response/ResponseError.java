package com.fournine.framework.response;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ResponseError {
	@ApiModelProperty(example = "INTERNAL_SERVER_ERROR")
	private HttpStatus statusCode;
	@ApiModelProperty(example = "NullPointerException")
	private String message;
	@ApiModelProperty(example = "null")
	private String details;
}