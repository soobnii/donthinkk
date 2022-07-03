package com.fournine.app.user.jpa.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fournine.app.revenue.jpa.entity.RevenueEntity;
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
@Table(name = "t_user")
public class UserEntity extends BaseEntity implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userNo;
	
	@Column(name="social_id")
	private String socialId;
	
	@Column(name="social_type")
	private String socialType;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@Column(name="introduction")
	private String introduction;
	
//	@Column(name="refresh_token")
//	private String refreshToken;
	
	@Column(name = "is_deleted")
	private String isDeleted;
	
	@Column(name = "is_public")
	private String isPublic;
	
	@OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY)
	private List<RevenueEntity> revenus;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
//		        for (Authority authority : authList) {
//		            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
//		        }
		authorities.add(new SimpleGrantedAuthority("USER"));
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return socialId;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
