package com.codi.laolaiqiao.web.controller.reqModel;

import com.codi.laolaiqiao.common.validation.MsgParams;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 对视频取消点赞/收藏
 * Created by song-jj on 2017/2/27.
 */
@Data
public class LikeOrStarCancelReq {
    /**
     * 视频ID
     */
    @MsgParams(value = {"视频ID"})
    @NotNull
    public Long videoId;

    /**
     * 类型。
     * 0：点赞；1：收藏
     */
    @MsgParams(value = {"类型"})
    @NotNull
    public String type;
}
