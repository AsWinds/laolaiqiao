package com.codi.laolaiqiao.web.controller.reqModel;

import lombok.Data;

/**
 * 请求基类
 * Created by song-jj on 2017/2/23.
 */
@Data
class BasePostReq {

    /**
     * Token
     */
    private String token;

    /**
     * 请求时间戳
     */
    private Long requestTime;

    /**
     * 签名
     */
    private String sign;
}
