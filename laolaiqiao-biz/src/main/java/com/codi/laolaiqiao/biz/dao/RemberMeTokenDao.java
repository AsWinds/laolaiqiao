package com.codi.laolaiqiao.biz.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;

import com.codi.laolaiqiao.api.domain.RemberMeToken;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;

public interface RemberMeTokenDao extends BaseRepository<RemberMeToken, Long> {

	RemberMeToken findByToken(String cookieToken);

	@Modifying
	void deleteByToken(String token);

	@Modifying
	int deleteByExpireAtBefore(Date aHourAgo);

}
