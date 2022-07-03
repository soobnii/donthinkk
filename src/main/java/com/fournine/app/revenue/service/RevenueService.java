package com.fournine.app.revenue.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.fournine.app.revenue.dto.RevenueDto;

public interface RevenueService {
	
	public HashMap<String, Object> getMyRevenues(int userNo) throws Exception;
	
	public RevenueDto getRevenue(int userNo, int revenueNo) throws Exception;
	
	public RevenueDto createRevenue(int userNo, RevenueDto revenueDto) throws Exception;
	
	public RevenueDto updateRevenue(int userNo, int revenueNo, RevenueDto revenueDto) throws Exception;
	
	public ResponseEntity<?> deleteRevenue(int userNo, int revenueNo) throws Exception;
	
	public List<RevenueDto> getOpenRevenues(Pageable pageable, int userNo) throws Exception;

	public List<RevenueDto> getUserRevenues(Pageable pageable, int userNo) throws Exception;

	public void updateRevenueClaim(int loginUserNo, int revenueNo) throws Exception;
	
}
