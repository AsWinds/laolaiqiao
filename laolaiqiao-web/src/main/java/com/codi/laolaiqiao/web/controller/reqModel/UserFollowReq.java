package com.codi.laolaiqiao.web.controller.reqModel;

import com.codi.laolaiqiao.common.validation.MsgParams;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 其他用户
 * Created by song-jj on 2017/3/6.
 */
@Data
public class UserFollowReq {

    @MsgParams("视频ID")
    @NotNull
    public Long videoId;

}
