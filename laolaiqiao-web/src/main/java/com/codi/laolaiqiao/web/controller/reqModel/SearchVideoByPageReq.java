package com.codi.laolaiqiao.web.controller.reqModel;

import javax.validation.constraints.Min;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.codi.laolaiqiao.common.validation.MsgParams;

/**
 *
 * Created by song-jj on 2017/2/22.
 */
@Data
public class SearchVideoByPageReq {

    /**
     * 一页的数量
     */
    public Integer size;

    /**
     * 第几页
     */
    @Min(0)
    public Integer page;

    /**
     * 排序
     */
    @MsgParams(value = {"排序字段"})
    @NotEmpty
    public String orderBy;
}
