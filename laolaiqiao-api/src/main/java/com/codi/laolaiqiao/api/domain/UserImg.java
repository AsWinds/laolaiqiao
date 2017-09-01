package com.codi.laolaiqiao.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.codi.laolaiqiao.common.entity.UploadDo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"bucket", "storedKey"})
	}
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserImg extends UploadDo {

	/**
	 * 上传图片者ID
	 */
	@Column(nullable = false, updatable = false)
	private Long userId;
	
	/**
	 * 
	 * 相册ID
	 * */
	@Column(nullable = false, updatable = false)
	private Long albumId;
	
	
	/**
	 * 初始化bucket, storedKey, expireTime, status, uploadComplete, originName字段
	 * 初始值: uploadComplete = false, status = STATUS_NOT_AUDIT, originName = null
	 * */
	public UserImg(Long userId, Long albumId, String bucket, String storedKey, Long expireTime){
		super(bucket, storedKey, expireTime);
		this.userId = userId;
		this.albumId = albumId;
	}

}
