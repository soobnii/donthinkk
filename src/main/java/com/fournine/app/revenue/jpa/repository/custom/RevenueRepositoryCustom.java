package com.fournine.app.revenue.jpa.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fournine.app.revenue.jpa.entity.RevenueEntity;

public interface RevenueRepositoryCustom {

	Page<RevenueEntity> findByIsPublicAndIsDeleted(String isPublic, String isDeleted, Pageable pageable);
	
	Page<RevenueEntity> findByUserEntity(int userNo, Pageable pageable);
}
