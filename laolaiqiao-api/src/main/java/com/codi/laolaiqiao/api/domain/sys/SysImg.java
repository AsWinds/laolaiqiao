package com.codi.laolaiqiao.api.domain.sys;

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
public class SysImg extends UploadDo {
	
	/**
	 * 初始化bucket, storedKey, expireTime, status, uploadComplete, originName字段
	 * 初始值: uploadComplete = false, status = STATUS_NOT_AUDIT, originName = null
	 * */
	public SysImg(String bucket, String storedKey, Long expireTime){
		super(bucket, storedKey, expireTime);
	}
}
