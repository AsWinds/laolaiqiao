package com.codi.laolaiqiao.sys.biz.service;

import com.codi.laolaiqiao.api.result.sys.SmsCodeResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 短信
 * Created by song-jj on 2017/3/15.
 */
public interface SmsService {
    /**
     * 分页查询所有短信
     * @return
     */
    Page<SmsCodeResult> getAllWithPage(Pageable page);
}
