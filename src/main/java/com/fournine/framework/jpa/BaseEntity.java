package com.fournine.framework.jpa;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity { // extends BaseCreateEntity 

	// Entity가 생성되어 저장될 때 시간이 자동 저장됩니다.
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime insertTimestamp;
	
	@LastModifiedDate
	private LocalDateTime updateTimestamp;
}