package com.fournine.app.revenue.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fournine.app.revenue.jpa.entity.RevenueEntity;
import com.fournine.app.revenue.jpa.repository.custom.RevenueRepositoryCustom;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueEntity, String>, RevenueRepositoryCustom {

	//@Query(value="select R from RevenueEntity R inner join UserEntity E where E.userNo=:userNo and R.isDeleted=:isDeleted")
	public Optional<List<RevenueEntity>> findByUserEntity_userNoAndIsDeleted(int userNo,String isDeleted);

	//@Query(value="select R from RevenueEntity R inner join UserEntity E where E.userNo=:userNo and R.revenueNo=:revenueNo")
	public Optional<RevenueEntity> findByUserEntity_userNoAndRevenueNo(int userNo, int revenueNo);
	
	public Optional<RevenueEntity> findByRevenueNo(int revenueNo);
}
