package com.codi.laolaiqiao.common.jdbc;

import java.io.Serializable;

import com.codi.laolaiqiao.common.entity.UploadDo;

public interface UploadDoDao<T extends UploadDo, ID extends Serializable> extends BaseRepository<T, ID> {

	T findByBucketAndStoredKey(String bucket, String fileKey);
	
}
