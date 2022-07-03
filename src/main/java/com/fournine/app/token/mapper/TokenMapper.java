package com.fournine.app.token.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

@MapperScan
@Repository
public interface TokenMapper {
	public int insertTokenInfo(String socialId, String socialType, String refreshToken);
	public int updateRefreshToken(String socialId, String socialType, String refreshToken);
	public int deleteRefreshToken(String socialId, String socialType);
	public String getTokenByUserInfo(String socialId, String socialType);
	public String getRefreshToken(String socialId, String socialType) throws Exception;
}
