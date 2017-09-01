package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.UserRequest;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;

/**
 * 用户请求DAO
 * Created by song-jj on 2017/2/24.
 */
public interface UserRequestDao extends BaseRepository<UserRequest, Long> {
	
	UserRequest findByUserId(Long userId);
	
}
