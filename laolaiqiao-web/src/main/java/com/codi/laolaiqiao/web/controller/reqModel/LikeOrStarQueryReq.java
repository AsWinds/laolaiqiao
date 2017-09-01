package com.codi.laolaiqiao.web.controller.reqModel;

import com.codi.laolaiqiao.common.validation.MsgParams;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 查询点赞或者收藏
 * Created by song-jj on 2017/2/27.
 */
@Data
public class LikeOrStarQueryReq {
    /**
     * 视频ID
     */
    @MsgParams(value = {"视频ID"})
    @NotNull
    public Long videoId;

}
