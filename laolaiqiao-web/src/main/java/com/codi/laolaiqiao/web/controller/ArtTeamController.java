package com.codi.laolaiqiao.web.controller;

import com.codi.laolaiqiao.api.service.ArtTeamService;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;
import com.laolaiqiao.web.servlet.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 团队接口（需要登录）
 * Created by song-jj on 2017/3/31.
 */
@RestController
@RequestMapping("/user/artTeam/")
public class ArtTeamController {

    @Autowired
    private ArtTeamService artService;

    /**
     * 根据团队ID取得用户详情
     * @param id
     * @return
     */
    @RequestMapping("/detail/{id}")
    public BaseResult activityDetail(@PathVariable("id") Long id){
        return new SingleDataResult<>(artService.get(id, UserUtil.getUserId()));
    }

    /**
     * 加入团队
     * @param teamId
     * @return
     */
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public BaseResult join(Long teamId){
        //return new CollectionResult<>(artService.getAll(pageable));
        return null;
    }
}
