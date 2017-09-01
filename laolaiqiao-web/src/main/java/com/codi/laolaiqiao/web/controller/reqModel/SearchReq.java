package com.codi.laolaiqiao.web.controller.reqModel;

import javax.validation.constraints.Min;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.codi.laolaiqiao.common.validation.MsgParams;

/**
 * 搜索参数
 */
@Data
public class SearchReq {

    /**
     * 关键词
     */
    @MsgParams(value = {"关键字"})
    @NotEmpty
    public String keyword;

    /**
     * 类型
     * 0：视频；1：艺术团
     */
    @MsgParams(value = {"类型"})
    @NotEmpty
    public String type;

    /**
     * 一页的数量
     */
    @Min(1)
    public Integer size;

    /**
     * 页数
     */
    @Min(0)
    public Integer page;

}
