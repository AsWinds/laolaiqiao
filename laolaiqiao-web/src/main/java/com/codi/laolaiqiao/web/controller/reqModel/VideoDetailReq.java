package com.codi.laolaiqiao.web.controller.reqModel;

import javax.validation.constraints.NotNull;

import lombok.Data;

import com.codi.laolaiqiao.common.validation.MsgParams;

/**
 * 搜索视频详情接口参数
 * Created by song-jj on 2017/2/22.
 */
@Data
public class VideoDetailReq {

    /**
     * 视频ID
     */
    @MsgParams(value = {"视频ID"})
    @NotNull
    public Long videoId;

    /**
     * 第几页
     */
    public Integer page;

    /**
     * 一页的数量
     */
    public Integer size;
}
