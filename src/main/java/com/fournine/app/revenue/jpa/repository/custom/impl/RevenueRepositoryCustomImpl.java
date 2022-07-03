package com.fournine.app.revenue.jpa.repository.custom.impl;

import static com.fournine.app.revenue.jpa.entity.QRevenueEntity.revenueEntity;
import static com.fournine.app.user.jpa.entity.QUserEntity.userEntity;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.fournine.app.revenue.jpa.entity.RevenueEntity;
import com.fournine.app.revenue.jpa.repository.custom.RevenueRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class RevenueRepositoryCustomImpl implements RevenueRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	public RevenueRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<RevenueEntity> findByIsPublicAndIsDeleted(String isPublic, String isDeleted, Pageable pageable) {
		List<RevenueEntity> entityList = queryFactory.selectFrom(revenueEntity)
				.leftJoin(userEntity)
				.on(revenueEntity.userEntity.userNo.eq(userEntity.userNo))
				.where(userEntity.isDeleted.eq(isDeleted), 
						userEntity.isPublic.eq(isPublic), 
						revenueEntity.isDeleted.eq(isDeleted), 
						revenueEntity.isPublic.eq(isPublic))
				.offset(pageable.getOffset()) //pageable의 getOffset은 page * size를 반환한다.
	 			.limit(pageable.getPageSize())
	 			.fetch();
		
		//return entityList;
		
		JPAQuery<RevenueEntity> countQuery = queryFactory
	 			.select(revenueEntity)
	 			.from(revenueEntity)
	 			.leftJoin(userEntity)
				.on(revenueEntity.userEntity.userNo.eq(userEntity.userNo))
				.where(userEntity.isDeleted.eq(isDeleted), 
						userEntity.isPublic.eq(isPublic), 
						revenueEntity.isDeleted.eq(isDeleted), 
						revenueEntity.isPublic.eq(isPublic));
		
		return PageableExecutionUtils.getPage(entityList, pageable, () -> countQuery.fetch().size());
	}
	
	@Override
	public Page<RevenueEntity> findByUserEntity(int userNo, Pageable pageable) {
		List<RevenueEntity> entityList = queryFactory.selectFrom(revenueEntity)
				.join(userEntity)
				.on(revenueEntity.userEntity.userNo.eq(userEntity.userNo))
				.where(userEntity.userNo.eq(userNo),
						userEntity.isDeleted.eq("F"), 
						userEntity.isPublic.eq("T"), 
						revenueEntity.isDeleted.eq("F"), 
						revenueEntity.isPublic.eq("T"))
				.offset(pageable.getOffset())
	 			.limit(pageable.getPageSize())
	 			.fetch();
		
		JPAQuery<RevenueEntity> countQuery = queryFactory
	 			.select(revenueEntity)
	 			.from(revenueEntity)
	 			.join(userEntity)
	 			.on(revenueEntity.userEntity.userNo.eq(userEntity.userNo))
				.where(userEntity.userNo.eq(userNo),
						userEntity.userNo.eq(userNo),
						userEntity.isDeleted.eq("F"), 
						userEntity.isPublic.eq("T"), 
						revenueEntity.isDeleted.eq("F"), 
						revenueEntity.isPublic.eq("T"));
		
		return PageableExecutionUtils.getPage(entityList, pageable, () -> countQuery.fetch().size());
	}
}
