package com.codi.laolaiqiao.api.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.codi.laolaiqiao.api.domain.Activity;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class ActivityResult {

    public static final String STATUS_SIGN_UP = "报名中";

    public static final String STATUS_FINISHED = "已截止";

	/**
	 *
	 * 活动名
	 *
	 * */
	private Long id;

	/**
	 *
	 * 活动名
	 *
	 * */
	private String name;

	/**
	 *
	 * 活动详情
	 *
	 * */
	private String detail;

	/**
	 *
	 * 活动图片地址
	 *
	 * */
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
	private String contact;

	/**
	 *
	 * 活动开始时间
	 *
	 * */
	@JSONField(format = "yyyy-MM-dd HH:mm")
	private Date startDate;

	/**
	 *
	 * 活动结束时间
	 *
	 * */
	@JSONField(format = "yyyy-MM-dd HH:mm")
	private Date endDate;

	/**
	 *
	 * 发布者ID, 对应SysUser表用户
	 *
	 * */
	private Long publisherId;

	/**
	 *
	 * 发布者用户名, 对应SysUser表用户
	 *
	 * */
	private String userName;

    /**
     * 状态
     */
	private String status = STATUS_SIGN_UP;

    /**
     * 图片文件名
     */
	private String imageFileName;


	public ActivityResult(Activity activity){
		this.contact = activity.getContact();
		this.detail = activity.getDetail();
		this.endDate = activity.getEndDate();
		this.id =activity.getId();
		this.imgUrl = activity.getImgUrl();
		if (this.imgUrl == null) {
	        this.imgUrl = QiNiuManager.getImgFileUrl(QiNiuManager.defaultActivityPhoto);
        } else {
            this.imgUrl = QiNiuManager.getImgFileUrl(this.imgUrl);
        }
		this.location = activity.getLocation();
		this.name = activity.getName();
		this.startDate = activity.getStartDate();
		this.imageFileName = activity.getImgUrl();
	}

	public ActivityResult(BigInteger id, String name, String detail, String imgUrl, String location,
			String contact, Date startDate, Date endDate, BigInteger publisherId, String userName){
		this.id = id.longValue();
		this.name = name;
		this.detail = detail;
		this.imgUrl = imgUrl;
		if (this.imgUrl == null) {
	        this.imgUrl = QiNiuManager.getImgFileUrl(QiNiuManager.defaultActivityPhoto);
        } else {
            this.imgUrl = QiNiuManager.getImgFileUrl(this.imgUrl);
        }
		this.location = location;
		this.contact = contact;
		this.startDate = startDate;
		this.endDate = endDate;
		this.publisherId = publisherId.longValue();
		this.userName = userName;
        this.imageFileName = imgUrl;
	}
}
