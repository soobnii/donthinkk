package com.fournine.app.staticInfo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fournine.app.staticInfo.jpa.entity.StaticEntity;
import com.fournine.app.staticInfo.jpa.repository.StaticRepository;
import com.fournine.app.staticInfo.service.StaticService;

@Service
@Transactional
public class StaticServiceImpl implements StaticService {

	@Autowired
	private StaticRepository staticRepository;
	
	@Override
	public HashMap<String, String> getStaticFiles(int order) throws Exception {
		HashMap<String ,String> infoList = new HashMap<>();
		
		// main 화면, 기준값, 사용여부(T)
		Optional<List<StaticEntity>> entityListOpt = staticRepository.findByCategoryAndOrderAndIsUse("main", order, "T");
		if(entityListOpt.isPresent()) {
			entityListOpt.get().forEach(entity -> {
				String kind = entity.getKind();
				infoList.put(kind, entity.getResCode()); // 같은 순서에 같은 키값이 있다면 덮어짐 (한개만 반환)
			});
		};
		
		return infoList;
	}

}
