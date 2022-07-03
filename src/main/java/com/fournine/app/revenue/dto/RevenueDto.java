package com.fournine.app.revenue.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fournine.app.user.dto.UserDto;

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
public class RevenueDto {
	
	private Integer revenueNo;
	
	private Integer revenue;
	
	private String title;
	
	private String subTitle;
	
	private String memo;
	
	private Date revenueDate;
	
	private String isDeleted;
	
	private String isPublic;
	
	private Integer userNo;
	
	private Integer categoryNo;
	
	private String categoryName;
	
	private String socialId;
	
	private String socialType;
	
	private String reportedUsers;
	
	private LocalDateTime insertTimestamp;
	
	private LocalDateTime updateTimestamp;
	
	private RevenueCategoryDto revenueCategory;
	
	private UserDto user;
}
