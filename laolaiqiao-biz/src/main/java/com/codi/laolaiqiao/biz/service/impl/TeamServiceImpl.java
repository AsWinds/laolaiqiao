package com.codi.laolaiqiao.biz.service.impl;

import com.codi.laolaiqiao.api.result.TeamUser;
import com.codi.laolaiqiao.api.service.TeamService;
import com.codi.laolaiqiao.biz.dao.ArtTeamDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private ArtTeamDao artTeamDao;

    @Autowired
    private UserDao userDao;

	@Override
	public Iterable<TeamUser> findTeamUsers(Long teamId) {

        return null;
    }

}
