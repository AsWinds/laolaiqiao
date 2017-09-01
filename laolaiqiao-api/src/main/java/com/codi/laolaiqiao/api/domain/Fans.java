package com.codi.laolaiqiao.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.codi.laolaiqiao.common.entity.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户/团队 粉丝（关注）
 * Created by song-jj on 2017/3/6.
 */
@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"fansId", "teamId", "userId"})
	}
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Fans extends Domain {

    /**
     * 常量 视频类型：个人
     */
    public static final int CATEGORY_PERSONAL = 0;

    /**
     * 常量 视频类型：团队
     */
    public static final int CATEGORY_TEAM = 1;

    /**
     * 类别。0：个人；1：团队
     */
    @Column(nullable = false, updatable = false)
    private Integer category;

    @Column(updatable = false)
    private Long userId;

    @Column(updatable = false)
    private Long teamId;

    @Column(nullable = false, updatable = false)
    private Long fansId;
    
    public static Fans userFans(Long fansId, Long userId){
    	return new Fans(CATEGORY_PERSONAL, userId, null, fansId);
    }
    
    public static Fans teamFans(Long fansId, Long teamId){
    	return new Fans(CATEGORY_TEAM, null, teamId, fansId);
    }
}
