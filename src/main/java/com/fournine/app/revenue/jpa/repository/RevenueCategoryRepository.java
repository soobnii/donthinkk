package com.fournine.app.revenue.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fournine.app.revenue.jpa.entity.RevenueCategoryEntity;

@Repository
public interface RevenueCategoryRepository extends JpaRepository<RevenueCategoryEntity, String> {

	public Optional<RevenueCategoryEntity> findByCategoryName(String categoryName);
	
}
