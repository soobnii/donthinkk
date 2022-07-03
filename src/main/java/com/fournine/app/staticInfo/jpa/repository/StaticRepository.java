package com.fournine.app.staticInfo.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fournine.app.staticInfo.jpa.entity.StaticEntity;

@Repository
public interface StaticRepository extends JpaRepository<StaticEntity, String> {

	public Optional<List<StaticEntity>> findByCategoryAndOrderAndIsUse(String category, int isDeleted, String isUse);
	
}
