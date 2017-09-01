package com.codi.laolaiqiao.web.controller.reqModel;

import javax.validation.constraints.Min;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.codi.laolaiqiao.common.validation.MsgParams;

/**
 * 查看更多艺术团 接口参数
 * Created by song-jj on 2017/2/16.
 */
@Data
public class MoreTeamReq {

    /**
     * 关键词
     */
    @MsgParams(value = {"关键字"})
    @NotEmpty
    public String keyword;

    /**
     * 一页的数量
     */
    public Integer size;

    /**
     * 页数
     */
    @Min(1)
    public Integer page;
}
