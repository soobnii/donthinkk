package com.fournine.app.revenue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fournine.app.revenue.dto.RevenueCategoryDto;
import com.fournine.app.revenue.service.RevenueCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "수익 카테고리 관리")
@RestController
@RequestMapping("/revenuecategories")
public class RevenueCategoryController {

	@Autowired
	private RevenueCategoryService categoryService;
	
	@ApiOperation(value="수익 카테고리 전체 조회")
	@GetMapping(value="")
	public ResponseEntity<List<RevenueCategoryDto>> getRevenueCategories() throws Exception {
		return ResponseEntity.ok(categoryService.getRevenueCategories());
	}
	
}
