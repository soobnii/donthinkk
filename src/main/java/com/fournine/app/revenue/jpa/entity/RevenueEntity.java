package com.fournine.app.revenue.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.fournine.app.user.jpa.entity.UserEntity;
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
@Table(name = "t_revenue_board")
@DynamicInsert
public class RevenueEntity extends BaseEntity {
	
	@Id
	@Column(name = "revenue_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer revenueNo;
	
	@Column(name = "revenue")
	private Integer revenue;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "sub_title")
	private String subTitle;
	
	@Column(name = "memo")
	private String memo;
	
	@Column(name = "revenue_date")
	private Date revenueDate;
	
	@Column(name = "is_deleted")
	private String isDeleted;
	
	@Column(name = "is_public")
	private String isPublic;
	
	@Column(name = "reported_users")
	private String reportedUsers;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="category_no")
	private RevenueCategoryEntity revenuCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_no", updatable=false)
	private UserEntity userEntity;
	
}
