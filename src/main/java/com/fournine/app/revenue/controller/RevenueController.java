package com.fournine.app.revenue.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fournine.app.revenue.dto.RevenueDto;
import com.fournine.app.revenue.service.RevenueService;
import com.fournine.app.user.jpa.entity.UserEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "수익 관리")
@RestController
@RequestMapping("/revenues")
public class RevenueController {

	@Autowired
	private RevenueService revenueService;
	
	private int getLoginUserNo(Authentication authentication) {
		UserEntity userDetails = (UserEntity)authentication.getPrincipal();
		int userNo = userDetails.getUserNo();
		return userNo;
	}
	
	@ApiOperation(value="내 수익리스트 조회")
	@GetMapping(value="/mine")
	public ResponseEntity<HashMap<String, Object>> getMyRevenues(Authentication authentication) throws Exception {
		return ResponseEntity.ok(revenueService.getMyRevenues(getLoginUserNo(authentication)));
	}
	
	@ApiOperation(value="내 수익 상세 조회")
	@GetMapping(value="/mine/{revenueNo}")
	public ResponseEntity<RevenueDto> getMyRevenue(Authentication authentication, 
				@PathVariable("revenueNo") int revenueNo) throws Exception {
		return ResponseEntity.ok(revenueService.getRevenue(getLoginUserNo(authentication), revenueNo));
	}
	
	@ApiOperation(value="수익 생성")
	@PostMapping(value="/mine")
	public ResponseEntity<?> createRevenue(Authentication authentication, 
			@RequestBody RevenueDto revenueDto) throws Exception {
		return ResponseEntity.ok(revenueService.createRevenue(getLoginUserNo(authentication), revenueDto));
	}
	
	@ApiOperation(value="수익 수정")
	@PutMapping(value="/mine/{revenueNo}")
	public ResponseEntity<?> updateRevenue(Authentication authentication, @PathVariable("revenueNo") int revenueNo,
			@RequestBody RevenueDto revenueDto) throws Exception {
		return ResponseEntity.ok(revenueService.updateRevenue(getLoginUserNo(authentication), revenueNo, revenueDto));
	}
	
	@ApiOperation(value="수익 삭제")
	@DeleteMapping(value="/mine/{revenueNo}")
	public ResponseEntity<?> deleteRevenue(Authentication authentication, @PathVariable("revenueNo") int revenueNo) throws Exception {
		return ResponseEntity.ok(revenueService.deleteRevenue(getLoginUserNo(authentication), revenueNo));
	}
	
	@ApiOperation(value="공개된 수익 피드 조회")
	@GetMapping(value="/all")
	public ResponseEntity<List<RevenueDto>> getOpenRevenues(Authentication authentication, @RequestParam("page") int page) throws Exception {
		PageRequest pageRequest = PageRequest.of(page, 10);
		return ResponseEntity.ok(revenueService.getOpenRevenues(pageRequest, getLoginUserNo(authentication)));
	}
	
	@ApiOperation(value="공개된 유저 수익 상세 조회")
	@GetMapping(value="/{userNo}/{revenueNo}")
	public ResponseEntity<RevenueDto> getUserRevenue(@PathVariable("userNo") int userNo, 
				@PathVariable("revenueNo") int revenueNo) throws Exception {
		return ResponseEntity.ok(revenueService.getRevenue(userNo, revenueNo));
	}
	
	@ApiOperation(value = "공개된 유저의 수익 리스트 가져오기")
	@GetMapping(value = "/{userNo}")
	public ResponseEntity<List<RevenueDto>> getUserRevenues(@RequestParam("page") int page, @PathVariable("userNo") int userNo) 
				throws Exception {
		PageRequest pageRequest = PageRequest.of(page, 10);
		return ResponseEntity.ok(revenueService.getUserRevenues(pageRequest, userNo));
	}
	
	@ApiOperation(value="수익 신고 (이미 공개된 수익만 신고 가능)")
	@PutMapping(value="/claim/{revenueNo}")
	public ResponseEntity<?> updateRevenueClaim(Authentication authentication, @PathVariable("revenueNo") int revenueNo) throws Exception {
		revenueService.updateRevenueClaim(getLoginUserNo(authentication), revenueNo);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
