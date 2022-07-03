package com.fournine.app.revenue.service;

import java.util.List;

import com.fournine.app.revenue.dto.RevenueCategoryDto;

public interface RevenueCategoryService {

	public List<RevenueCategoryDto> getRevenueCategories() throws Exception;
	
}
