package com.codi.laolaiqiao.api.result.sys;

import com.codi.base.util.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 * 短信验证码
 * Created by song-jj on 2017/3/15.
 */
@Data
public class SmsCodeResult {

    public SmsCodeResult(Date createdAt, String code, String phone) {
        this.sendTime = DateUtils.formatDateTime(createdAt);
        this.code = code;
        this.phone = phone;
    }

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 短信验证码
     */
    private String code;

    /**
     * 手机号码
     */
    private String phone;

}
