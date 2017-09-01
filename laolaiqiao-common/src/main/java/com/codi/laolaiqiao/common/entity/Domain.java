package com.codi.laolaiqiao.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * 表结构公用字段
 *
 * */
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = false)
public class Domain {

	/**
	 *
	 * 主键
	 *
	 * */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	/**
	 *
	 * 版本号
	 *
	 * */
	@JSONField(serialize = false)
	@Version
	private int version;

	/**
	 *
	 * 记录创建时间戳
	 *
	 * */
	@JSONField(serialize = false)
	@CreatedDate
	@Column(updatable = false, nullable = false)
	private Date createdAt;

	/**
	 *
	 * 记录上次修改时间戳
	 *
	 * */
	@JSONField(serialize = false)
	@LastModifiedDate
	@Column(nullable = false)
	private Date lastUpdateTime;

	@PrePersist
	void setCreatedAt(){
		this.createdAt = new Date();
		this.lastUpdateTime = new Date();
	}

	@PreUpdate
	void setLastUpdateTime(){
		this.lastUpdateTime = new Date();
	}

}
