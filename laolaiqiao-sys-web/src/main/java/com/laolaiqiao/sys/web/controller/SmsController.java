package com.laolaiqiao.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.sys.biz.service.SmsService;
import com.laolaiqiao.sys.web.result.SysResult;

/**
 * 后台 短信管理
 * Created by song-jj on 2017/3/15.
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 取得所有短信
     * @param page
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public BaseResult all(@PageableDefault(sort = { "createdAt" }, direction = Sort.Direction.DESC)
                                         Pageable page){
        return new SysResult(smsService.getAllWithPage(page));
    }
}
