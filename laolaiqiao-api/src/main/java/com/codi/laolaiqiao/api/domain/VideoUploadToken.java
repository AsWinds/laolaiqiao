package com.codi.laolaiqiao.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.codi.laolaiqiao.api.req.QiNiuUploadCallback;
import com.codi.laolaiqiao.common.entity.UploadDo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 
 * 创建上传token的时候并不知道将要上传的视频文件的后缀名,
 * 上传文件的后缀名将会在上传完毕后在通过七牛云的回调被告知.
 * 视频上传流程如下: 视频双传 ---> 审核通过 ---> 视频转码 ---> 发布到UploadedVideo表
 * 
 * 
 * */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"bucket", "storedKey"})
	}
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VideoUploadToken extends UploadDo {
    /**
     * 视频状态: -1已删除(审核未通过, 或者视频最终上传失败); 1, 未审核; 3, 已审核; 5, 数据处理中;
     */
	public static final int STATUS_AUDIT_PFOP = 5;
	
	/**
	 * 
	 * 新建一个团队视频token记录
	 * 
	 * */
	public VideoUploadToken(String title, Long userId, Long ownerTeamId, String bucket, String storedKey, Long expireTime){
		this(title, userId, bucket, storedKey, expireTime);
		this.ownerTeamId = ownerTeamId;
	}
	
	/**
	 * 
	 * 新建一个个人视频token记录
	 * 
	 * */
	public VideoUploadToken(String title, Long userId, String bucket, String storedKey, Long expireTime){
		super(bucket, storedKey, expireTime);
		this.userId = userId;
		this.title = title;
	}

	/**
	 * 
	 * 用户ID
	 * 
	 * */
	@Column(nullable = false, updatable = false)
	private Long userId;
	
	/**
	 * 
	 * 视频名称
	 * 
	 * */
	@Column(nullable = false, updatable = false)
	private String title;

	/**
	 *
	 * 该视频归属的team
	 *
	 * */
	@Column(updatable = false)
	private Long ownerTeamId;
	
	/**
	 * 
	 * 原始视频文件的扩展名
	 * 
	 */
	@Column
	private String ext;
	
	/**
	 * 
	 * 七牛计算的原视频文件的hash值
	 * 
	 */
	@Column
	private String fileHash;
	
    /**
     * 审核视频者ID, 管理系统用户表ID
     */
	@Column
	private Long auditUserId;
	
	
	/**
	 * 
	 * 根据七牛回调返回的结果更新VideoUploadToken对象的值, 并返回该对象 
	 * 
	 * */
	public VideoUploadToken onUploadComplete(QiNiuUploadCallback vuc){
		this.onUploadComplete(vuc.fname);
		this.ext = vuc.ext;
		this.fileHash = vuc.hash;
		return this;
	}
}
