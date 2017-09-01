package com.laolaiqiao.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.sys.biz.result.UserMsgSearchResult;
import com.codi.laolaiqiao.sys.biz.service.UserMsgService;
import com.laolaiqiao.sys.web.result.SysResult;

/**
 * 用户后台管理
 * Created by song-jj on 2017/3/14.
 */
@RestController
@RequestMapping(value = "/user")
public class UserMsgController {

    @Autowired
    private UserMsgService userMsgService;

    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public BaseResult query(String userName, Pageable page){
        Page<UserMsgSearchResult> users = userMsgService.search(userName, page);
        return new SysResult(users);
    }

    /**
     * 保存用户
     * @param id
     * @param phone
     * @param userName
     * @param address
     * @param idNo
     * @param teamId
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public BaseResult save(Long id, String phone, String userName, String address, String idNo, Long teamId){
        userMsgService.save(id, phone, userName, address, idNo, teamId);
        return new SysResult(true);
    }

    /**
     * 更新状态
     * @param id
     * @param status 0：正常；1：禁用
     * @return
     */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    public BaseResult updateStatus(Long id, Integer status){
        userMsgService.updateStatus(id, status);
        return new SysResult(true);
    }

    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = "queryUserById", method = RequestMethod.GET)
    public BaseResult queryUserById(Long id){
        return new SysResult(userMsgService.queryUserById(id));
    }
}
