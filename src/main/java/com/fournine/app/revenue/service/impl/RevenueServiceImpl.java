package com.fournine.app.revenue.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fournine.app.revenue.dto.RevenueDto;
import com.fournine.app.revenue.jpa.entity.RevenueCategoryEntity;
import com.fournine.app.revenue.jpa.entity.RevenueEntity;
import com.fournine.app.revenue.jpa.repository.RevenueCategoryRepository;
import com.fournine.app.revenue.jpa.repository.RevenueRepository;
import com.fournine.app.revenue.service.RevenueService;
import com.fournine.app.staticInfo.service.StaticService;
import com.fournine.app.user.jpa.entity.UserEntity;
import com.fournine.app.user.jpa.repository.UserRepository;
import com.fournine.framework.exception.business.BusinessException;
import com.fournine.framework.exception.business.ExceptionCode;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
public class RevenueServiceImpl implements RevenueService {

	@Autowired
	private RevenueRepository revenueRepository;
	
	@Autowired
	private RevenueCategoryRepository categoryRepository;
	
	@Autowired
	private StaticService staticService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public HashMap<String, Object> getMyRevenues(int userNo) throws Exception {
		HashMap<String, Object> revenueMap = new HashMap<>();
		List<RevenueDto> revenueDtoList = new ArrayList<RevenueDto>();
		
		Optional<List<RevenueEntity>> revenueEntityList = revenueRepository.findByUserEntity_userNoAndIsDeleted(userNo, "F");
		int revenueSize = 0;
		if(revenueEntityList.isPresent()) {
			revenueSize = revenueEntityList.get().size();
			revenueEntityList.get().forEach(entity -> {
				RevenueDto dto = new RevenueDto();
				dto = modelMapper.map(entity, RevenueDto.class);
				revenueDtoList.add(mapEntityToDto(entity, dto));
			});
		};
		revenueDtoList.sort(Comparator.comparing(RevenueDto::getInsertTimestamp).reversed());
		
		revenueMap.put("staticFiles", getStaticFilesInfo(revenueSize));
		revenueMap.put("revenueList", revenueDtoList);
		return revenueMap;
	}

	@Override
	public RevenueDto getRevenue(int userNo, int revenueNo) throws Exception {
		RevenueDto revenueDto = new RevenueDto();
		
		Optional<RevenueEntity> revenueEntity = revenueRepository.findByUserEntity_userNoAndRevenueNo(userNo, revenueNo);
		if(revenueEntity.isPresent()) {
			revenueDto = modelMapper.map(revenueEntity.get(), RevenueDto.class);
			revenueDto = mapEntityToDto(revenueEntity.get(), revenueDto);
		}
		
		return revenueDto;
	}
	
	@Override
	public RevenueDto createRevenue(int userNo, RevenueDto revenueDto) throws Exception {
		String categoryName = revenueDto.getCategoryName();
		Optional<RevenueCategoryEntity> category = categoryRepository.findByCategoryName(categoryName);
		Optional<UserEntity> user = userRepository.findByUserNo(userNo);
		
		if(category.isPresent() && user.isPresent()) {
			RevenueEntity revenueEntity = modelMapper.map(revenueDto, RevenueEntity.class);
			revenueEntity.setUserEntity(user.get());
			revenueEntity.setRevenuCategory(category.get());
			revenueEntity.setIsDeleted("F");
			revenueRepository.save(revenueEntity);
		} else {
			throw new BusinessException("잘못된 파라미터입니다.", ExceptionCode.USER_NOT_FOUND);
		}
		
		return revenueDto;
	}

	@Override
	public RevenueDto updateRevenue(int userNo, int revenueNo, RevenueDto revenueDto) throws Exception {
		Optional<RevenueEntity> entity = revenueRepository.findByUserEntity_userNoAndRevenueNo(userNo, revenueNo);
		
		if(entity.isPresent()) {
			RevenueEntity revenueEntity = entity.get();
			
			String categoryName = revenueDto.getCategoryName();
			Optional<RevenueCategoryEntity> category = categoryRepository.findByCategoryName(categoryName);
			
			revenueEntity.setTitle(revenueDto.getTitle())
				.setSubTitle(revenueDto.getSubTitle())
				.setRevenue(revenueDto.getRevenue())
				.setMemo(revenueDto.getMemo())
				.setRevenueDate(revenueDto.getRevenueDate())
				.setIsPublic(revenueDto.getIsPublic())
				.setRevenuCategory(category.get());
			revenueRepository.save(revenueEntity);
		} else {
			throw new BusinessException(ExceptionCode.REVENUE_NOT_FOUND);
		}
		
		return revenueDto;
	}

	@Override
	public ResponseEntity<?> deleteRevenue(int userNo, int revenueNo) throws Exception {
		Optional<RevenueEntity> entity = revenueRepository.findByUserEntity_userNoAndRevenueNo(userNo, revenueNo);
		
		if(entity.isPresent()) {
			RevenueEntity revenueEntity = entity.get();
			revenueEntity.setIsDeleted("T");
			revenueRepository.save(revenueEntity);
		} else {
			throw new BusinessException(ExceptionCode.REVENUE_NOT_FOUND);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public List<RevenueDto> getOpenRevenues(Pageable pageable, int userNo) throws Exception {;
		List<RevenueDto> revenueDtoList = new ArrayList<>();
		
		Page<RevenueEntity> entityList = revenueRepository.findByIsPublicAndIsDeleted("T", "F", pageable);
		entityList.forEach(entity -> {
			String[] reportedUsers = {};
			if(!ObjectUtils.isEmpty(entity.getReportedUsers())) {
				reportedUsers = entity.getReportedUsers().split(",");
			}
			if(!Arrays.asList(reportedUsers).contains(Integer.toString(userNo))) {
				RevenueDto dto = new RevenueDto();
				dto = modelMapper.map(entity, RevenueDto.class);
				revenueDtoList.add(mapEntityToDto(entity, dto));
			}
		});
		revenueDtoList.sort(Comparator.comparing(RevenueDto::getInsertTimestamp).reversed());
		return revenueDtoList;
	}
	
	private RevenueDto mapEntityToDto(RevenueEntity entity, RevenueDto dto) {
		dto.setCategoryName(entity.getRevenuCategory().getCategoryName());
		dto.setUserNo(entity.getUserEntity().getUserNo());
		dto.setSocialId(entity.getUserEntity().getSocialId());
		dto.setSocialType(entity.getUserEntity().getSocialType());
		return dto;
	}
	
	private HashMap<String, String> getStaticFilesInfo(int revenuesSize) throws Exception {
		int order = 0;
		switch (revenuesSize) {
		case 0:
			order = 0;
			break;
		case 1:
		case 2:
			order = 2;
			break;
		case 3:
		case 4: 
			order = 4;
			break;
		default:
			order = 5;
			break;
		}
		return staticService.getStaticFiles(order);
	}

	@Override
	public List<RevenueDto> getUserRevenues(Pageable pageable, int userNo) throws Exception {
		List<RevenueDto> revenueDtoList = new ArrayList<>();
		
		Page<RevenueEntity> entityList = revenueRepository.findByUserEntity(userNo, pageable);
		entityList.forEach(entity -> {
			RevenueDto dto = new RevenueDto();
			dto = modelMapper.map(entity, RevenueDto.class);
			revenueDtoList.add(mapEntityToDto(entity, dto));
		});
		revenueDtoList.sort(Comparator.comparing(RevenueDto::getInsertTimestamp).reversed());
		return revenueDtoList;
	}

	@Override
	public void updateRevenueClaim(int loginUserNo, int revenueNo) throws Exception {
		Optional<RevenueEntity> entity = revenueRepository.findByRevenueNo(revenueNo);
		
		if(entity.isPresent()) {
			RevenueEntity revenueEntity = entity.get();
			
			String updateReports = revenueEntity.getReportedUsers();
			
			String[] reportedUsers = {};
			ArrayList<String> list = new ArrayList<>();
			if(!ObjectUtils.isEmpty(updateReports)) {
				reportedUsers = updateReports.split(",");
				list = new ArrayList<>(Arrays.asList(reportedUsers));
			}
			
			if(!list.contains(Integer.toString(loginUserNo))) {
				String str = Integer.toString(loginUserNo);
				list.add(str);
			}
			
			revenueEntity.setReportedUsers(String.join(",", list));
			revenueRepository.save(revenueEntity);
		} else {
			throw new BusinessException(ExceptionCode.REVENUE_NOT_FOUND);
		}
	}
}
