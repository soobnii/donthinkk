package com.fournine.app.revenue.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fournine.app.revenue.dto.RevenueCategoryDto;
import com.fournine.app.revenue.jpa.entity.RevenueCategoryEntity;
import com.fournine.app.revenue.jpa.repository.RevenueCategoryRepository;
import com.fournine.app.revenue.service.RevenueCategoryService;

@Service
@Transactional
public class RevenueCategoryServiceImpl implements RevenueCategoryService {

	@Autowired
	private RevenueCategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<RevenueCategoryDto> getRevenueCategories() throws Exception {
		List<RevenueCategoryDto> dtoList = new ArrayList<>();
		
		List<RevenueCategoryEntity> entityList = categoryRepository.findAll();	
		if(entityList != null) {
			entityList.forEach(entity -> {
				dtoList.add(modelMapper.map(entity, RevenueCategoryDto.class));
			});
		}
		return dtoList;
	}

	
}
