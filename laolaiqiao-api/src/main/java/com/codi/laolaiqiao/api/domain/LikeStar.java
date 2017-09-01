package com.codi.laolaiqiao.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.codi.laolaiqiao.common.entity.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 点赞收藏
 * Created by song-jj on 2017/2/22.
 */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LikeStar extends Domain {

    /**
     * 视频ID
     */
	@Column(nullable = false, updatable = false)
    private Long videoId;

    /**
     * 用户ID
     */
	@Column(nullable = false, updatable = false)
    private Long userId;

    /**
     * 类型
     * 0：点赞，1：收藏
     */
	@Column(nullable = false, updatable = false)
    private String type;

    public LikeStar(String type) {
        this.type = type;
    }

}
