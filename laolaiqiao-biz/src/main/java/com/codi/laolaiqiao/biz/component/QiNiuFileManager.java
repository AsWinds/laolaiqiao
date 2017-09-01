package com.codi.laolaiqiao.biz.component;

import org.springframework.stereotype.Component;

import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;

@Component
public class QiNiuFileManager extends QiNiuManager {
	
	private BucketManager bucketManager;

	public QiNiuFileManager() {
		super();
		Configuration cfg = new Configuration(Zone.autoZone());
		bucketManager = new BucketManager(auth, cfg);
	}
	
	public FileInfo getFileInfo(String bucket, String fileKey){
		try {
	        return bucketManager.stat(bucket, fileKey);
        } catch (QiniuException e) {
        	Response response = e.response;
        	//无文件的状态码
        	if (response.statusCode == 612) {
	            return null;
            }
	        throw new BaseException("9003");
        }
	}
}
