package com.fournine.app.staticInfo.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fournine.app.staticInfo.service.StaticService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "static files")
@RestController
@RequestMapping("/staticfiles")
public class StaticController {

	@Autowired
	private StaticService staticService;
	
	@ApiOperation(value="메인화면 정적파일 정보 받기")
	@GetMapping(value="/main")
	public ResponseEntity<HashMap<String, String>> getStaticFiles(@RequestParam("order") int order) throws Exception {
		return ResponseEntity.ok(staticService.getStaticFiles(order));
	}
}
