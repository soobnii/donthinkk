package com.fournine.app.token.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Getter
@Setter
public class TokenDto {
	@ApiModelProperty(example = "eyJTT0NJQUxUWVBFIjoia2FrYW8iLCJleHAiOjE2NDkzNTA4ODUsIlZBTElESVRZIjoxODAwMCwiaWF0IjoxNjQ5MzMyODg1LCRucTbuRUCtwEp_rSl-BIX-9SgKfiqKk89STcdQoFuuft67-mqeSxcShqhWhi_R_9g")
	private String accessToken;
	@ApiModelProperty(example = "1649355522000")
	private Date accessTokenExpiration;
	@ApiModelProperty(example = "eyJhbGciOiJIUzUxMiJ9.eyJTT0NJQUxUWVBFIjoia2FrYTW8iLCJleHAiOjE2NTA1NDI0ODUsIlZBTElESVRZIjoxMDMqdblyiGT2NVrt5hCan9RisPt-7ydlSp-nOkS3z1DWXABJMSzdAWG9TzV2mGWQZwkH3V0fbQ")
	private String refreshToken;
	@ApiModelProperty(example = "1650547122000")
	private Date refreshTokenExpiration;

	@Getter
	public static class PostReqTokenDto {
		@ApiModelProperty(example = "ggPGqL451LFJU6kT0SIoQHL7q9D2K9fJsYl2odeuZ8h6ws4Kp22SXHkAyU-qf-yWlje5")
		private String refreshToken;
	}
}
