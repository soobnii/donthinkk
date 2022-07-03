package com.fournine.app.staticInfo.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "t_static_files", 
	indexes = @Index(name = "t_static_files_category_IDX", columnList = "category"))
public class StaticEntity {

	@Id
	@Column(name = "static_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer staticNo;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "order")
	private Integer order;
	
	@Column(name = "kind")
	private String kind;
	
	@Column(name = "res_code")
	private String resCode;
	
	@Column(name = "is_use")
	private String isUse;
	
}
