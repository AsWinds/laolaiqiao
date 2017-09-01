package com.codi.laolaiqiao.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.codi.laolaiqiao.common.entity.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VideoComment extends Domain {

    /**
     * 评论内容
     */
	@Column(nullable = false, updatable = false)
	private String comment;

    /**
     * 用户ID
     */
	@Column(nullable = false, updatable = false)
	private Long userId;

    /**
     * 视频ID
     */
	@Column(nullable = false, updatable = false)
	private Long videoId;

    /**
     * 是否合法
     */
	@Column(nullable = false)
	private Boolean isValid;
}
