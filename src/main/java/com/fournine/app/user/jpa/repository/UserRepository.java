package com.fournine.app.user.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fournine.app.user.jpa.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>  {

	//public UserEntity findBySocialIdAndSocialTypeAndIsDeletedFalse(String socialId, String socialType);
	
	public UserEntity findBySocialIdAndSocialType(String socialId, String socialType);
	
	public Optional<UserEntity> findByUserNo(int userNo);
	
	public Optional<List<UserEntity>> findByIsPublic(String isPublic);
}
