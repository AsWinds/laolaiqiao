package com.codi.laolaiqiao.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.codi.laolaiqiao.common.entity.Domain;

/**
 * 
 * 用户参加的活动表
 * 
 * */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserJoinedActivity extends Domain {
	
	/**
	 * 
	 * 
	 * */
	@Column(nullable = false, updatable = false)
	private Long userId;

	@Column(nullable = false, updatable = false)
	private Long activityId;
	
}
