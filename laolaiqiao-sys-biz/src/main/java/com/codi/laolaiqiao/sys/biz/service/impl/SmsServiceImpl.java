package com.codi.laolaiqiao.sys.biz.service.impl;

import com.codi.laolaiqiao.api.result.sys.SmsCodeResult;
import com.codi.laolaiqiao.biz.dao.SmsCodeDao;
import com.codi.laolaiqiao.sys.biz.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 后台 短信查询
 * Created by song-jj on 2017/3/15.
 */
@Service
@Transactional
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsCodeDao smsCodeDao;

    /**
     * 分页查询所有短信
     *
     * @param page
     * @return
     */
    @Override
    public Page<SmsCodeResult> getAllWithPage(Pageable page) {
        return smsCodeDao.findAllWithPage(page);
    }
}
