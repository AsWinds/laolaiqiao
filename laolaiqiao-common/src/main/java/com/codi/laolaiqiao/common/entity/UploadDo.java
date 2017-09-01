package com.codi.laolaiqiao.common.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
*
* 表结构公用字段
*
* */
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class UploadDo extends Domain {
	public static final int STATUS_DEL = -1;
	public static final int STATUS_NOT_AUDIT = 1;
	public static final int STATUS_AUDIT_PASS = 3;
	
	/**
	 * 存储的bucket
	 * 
	 */
	@Column(nullable = false, updatable = false)
	private String bucket;
	
	/**
	 * 
	 * 在七牛上存储的名称, 注意, 此时没有后缀
	 * 
	 * */
	@Column(nullable = false, updatable = false)
	private String storedKey;
	
	/**
	 * 
	 * token过期时间,以秒为单位
	 * 
	 * */
	private Long expireTime;
	
	/**
	 * 
	 * 是否上传完毕
	 * 
	 * */
	@Column(nullable = false)
	private Boolean uploadComplete;
	
	/**
	 * 视频的原始文件名
	 * 
	 */
	@Column
	private String originName;
	
    /**
     * 记录状态: -1已删除(审核未通过, 或者视频最终上传失败); 1, 未审核; 3, 已审核; 5, 数据处理中;
     */
	@Column(nullable = false)
	private Integer status;
	
	/**
	 * 初始化bucket, storedKey, expireTime, status, uploadComplete, originName字段
	 * 初始值: uploadComplete = false, status = STATUS_NOT_AUDIT, originName = null
	 * */
	public UploadDo(String bucket, String storedKey, Long expireTime){
		this.bucket = bucket;
		this.storedKey = storedKey;
		this.expireTime = expireTime;
		this.uploadComplete = false;
		this.status = STATUS_NOT_AUDIT;
		this.originName = null;
	}
	
	/**
	 * 上传完成, 更新uploadComplete字段和originName字段
	 * 更新后: uploadComplete = true;
	 * */
	public void onUploadComplete(String originName){
		this.uploadComplete = true;
		this.originName = originName;
	}
}
