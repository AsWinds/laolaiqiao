package com.codi.laolaiqiao.web.controller.reqModel;

import com.codi.laolaiqiao.common.validation.MsgParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 用户评论
 * Created by song-jj on 2017/2/23.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VideoCommentReq extends BasePostReq {

    /**
     * 视频ID
     */
    public Long videoId;

    /**
     * 评论
     */
    @MsgParams(value = {"评论内容"})
    @NotEmpty
    public String comment;
}
