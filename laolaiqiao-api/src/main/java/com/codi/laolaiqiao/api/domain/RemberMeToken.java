package com.codi.laolaiqiao.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.codi.laolaiqiao.common.entity.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * rember me toke 表
 *
 * */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RemberMeToken extends Domain {

	/**
	 *
	 * 对应的登录用户userId
	 *
	 * */
	@Column(updatable = false, nullable = false)
	private long userId;

	/**
	 *
	 * 登陆态token
	 *
	 * */
	@Column(nullable = false)
	private String token;

	/**
	 *
	 * 过期时间
	 *
	 * */
	@Column(nullable = false)
	private Date expireAt;



}
