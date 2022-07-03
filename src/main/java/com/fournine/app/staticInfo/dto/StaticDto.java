package com.fournine.app.staticInfo.dto;

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
public class StaticDto {

	private Integer staticNo;
	
	private String category;
	
	private Integer order;
	
	private String kind;
	
	private String resCode;
	
	private String isUse;
	
}
