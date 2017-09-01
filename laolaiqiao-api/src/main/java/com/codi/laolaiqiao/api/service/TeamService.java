package com.codi.laolaiqiao.api.service;

import com.codi.laolaiqiao.api.result.TeamUser;

public interface TeamService {

    Iterable<TeamUser> findTeamUsers(Long teamId);


}
