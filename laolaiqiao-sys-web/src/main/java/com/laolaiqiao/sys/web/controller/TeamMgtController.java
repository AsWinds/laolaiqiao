package com.laolaiqiao.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.result.SearchResult;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.sys.biz.service.TeamMgtService;
import com.laolaiqiao.sys.web.result.SysResult;

/**
 * 团队详情
 * Created by song-jj on 2017/3/14.
 */
@RestController
@RequestMapping(value = "/team")
public class TeamMgtController {

    @Autowired
    private TeamMgtService teamMgtService;

    /**
     *团队查询
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public BaseResult search(String teamName, Pageable page){
        Page<SearchResult> result = teamMgtService.search(page, teamName);
        return new SysResult(result);
    }

    /**
     * 团队列表
     * @param teamName
     * @param page
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult all(String teamName, Pageable page){
        Page<ArtTeamSearchResult> result = teamMgtService.getTeamList(page, teamName);
        return new SysResult(result);
    }

    /**
     * 保存团队
     * @param id 团队ID
     * @param name 团队名称
     * @param address 地点
     * @param leaderId 团长ID
     * @param detail 团队介绍
     * @param imageFileName 团队图片
     * @return 保存结果
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public BaseResult save(Long id, String name, String address, Long leaderId, String detail , String imageFileName){
        // 保存
        teamMgtService.save(id, name, address, leaderId, detail, imageFileName);
        return new BaseResult(true);
    }

    /**
     * 根据团队ID取得团队详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public BaseResult detail(@PathVariable("id") Long id){
        return new SysResult(teamMgtService.detail(id));
    }

    /**
     * 删除活动
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public BaseResult delete(@PathVariable("id") Long id){
        teamMgtService.delete(id);
        return new BaseResult(true);
    }

}
