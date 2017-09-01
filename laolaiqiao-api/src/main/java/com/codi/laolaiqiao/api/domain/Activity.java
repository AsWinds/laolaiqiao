package com.codi.laolaiqiao.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.codi.laolaiqiao.common.entity.Domain;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 
 * 活动表
 * 
 * */
@Setter
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Activity extends Domain {
	
	/**
	 * 
	 * 活动名
	 * 
	 * */
	@Column(nullable = false)
	private String name;
	
	/**
	 * 
	 * 活动详情
	 * 
	 * */
	@Column(nullable = false)
	private String detail;
	
	/**
	 * 
	 * 活动图片地址
	 * 
	 * */
	@Column(nullable = false)
	private String imgUrl;
	
	/**
	 * 
	 * 举办地点
	 * 
	 * */
	private String location;
	
	/**
	 * 
	 * 联系方式
	 * 
	 * */
	@Column(nullable = false)
	private String contact;
	
	/**
	 * 
	 * 活动开始时间
	 * 
	 * */
	@JSONField(format = "yyyy-MM-dd")
	@Column(nullable = false)
	private Date startDate;
	
	/**
	 * 
	 * 活动结束时间
	 * 
	 * */
	@JSONField(format = "yyyy-MM-dd")
	@Column(nullable = false)
	private Date endDate;
	
	/**
	 * 
	 * 发布者ID, 对应SysUser表用户
	 * 
	 * */
	@Column(updatable = false, nullable = false)
	private Long publisherId;
	
    /**
     * 设值
     * @param imageUrl
     * @param name
     * @param startDate
     * @param endDate
     * @param location
     * @param detail
     * @param contact
     * @param userId
     * @param activity
     */
	public Activity(String imageUrl, String name, Date startDate, Date endDate, String location, String detail, String contact, Long userId){
		update(imageUrl, name, startDate, endDate, location, detail, contact, userId);
	}
	
	public Activity update(String imageUrl, String name, Date startDate, Date endDate, String location, String detail, String contact, Long userId){
        this.setName(name); // 活动名称
        this.setImgUrl(imageUrl == null ? QiNiuManager.defaultActivityPhoto : imageUrl);// 活动图片URL
        this.setStartDate(startDate);// 活动开始日期
        this.setLocation(location);// 活动地点
        this.setDetail(detail);// 活动详情
        this.setEndDate(endDate);// 活动截止日期
        this.setPublisherId(userId);// 活动发布者
        this.setContact(contact);// 联系方式
        return this;
	}
	
}
