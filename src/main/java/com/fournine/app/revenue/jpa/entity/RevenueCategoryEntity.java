package com.fournine.app.revenue.jpa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fournine.framework.jpa.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "t_revenue_category")
public class RevenueCategoryEntity extends BaseEntity {
	
	@Id
	@Column(name = "category_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer categoryNo;
	
	@Column(name = "category_name")
	private String categoryName;

//	@Column(name = "is_use")
//	private String isUse;
	
	@OneToMany(mappedBy = "revenuCategory", fetch = FetchType.LAZY)
	private List<RevenueEntity> revenus;
}
