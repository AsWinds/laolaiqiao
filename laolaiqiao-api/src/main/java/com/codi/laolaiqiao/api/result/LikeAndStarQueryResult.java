package com.codi.laolaiqiao.api.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.codi.laolaiqiao.common.web.result.BaseResult;

/**
 * Created by song-jj on 2017/2/27.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LikeAndStarQueryResult extends BaseResult {

    /**
     * 当前用户是否对该视频点赞
     */
    private Boolean isLiked;

    /**
     * 当前用户是否收藏该视频
     */
    private Boolean isStarred;
}
