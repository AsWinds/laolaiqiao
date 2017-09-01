package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.SmsCode;
import com.codi.laolaiqiao.api.result.sys.SmsCodeResult;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface SmsCodeDao extends BaseRepository<SmsCode, Long>{

	SmsCode findFirstByPhoneOrderByIdDesc(String phone);

    @Query("select new com.codi.laolaiqiao.api.result.sys.SmsCodeResult(a.createdAt, a.smsCode, a.phone) from SmsCode a ")
    Page<SmsCodeResult> findAllWithPage(Pageable page);
}
