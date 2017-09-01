package com.codi.laolaiqiao.api.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 点赞收藏 的返回结果
 * Created by song-jj on 2017/3/1.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class LikeAndStarClickResult {

    /**
     * 此次行为是否生效
     */
    private boolean effect = false;

    public LikeAndStarClickResult(boolean effect) {
        this.effect = effect;
    }
}
