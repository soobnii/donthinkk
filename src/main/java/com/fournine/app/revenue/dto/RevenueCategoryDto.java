package com.fournine.app.revenue.dto;

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
public class RevenueCategoryDto {

	private Integer categoryNo;

	private String categoryName;
	
	/*
	private String isUse;
	
	private LocalDateTime insertTimestamp;
	
	private LocalDateTime updateTimestamp;
	*/
}
