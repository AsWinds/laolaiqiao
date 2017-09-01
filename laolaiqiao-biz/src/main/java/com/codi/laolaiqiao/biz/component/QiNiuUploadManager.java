package com.codi.laolaiqiao.biz.component;

import org.springframework.stereotype.Component;

import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.qiniu.util.StringMap;

@Component
public class QiNiuUploadManager extends QiNiuManager {
	
	public QiNiuUploadManager() {
		super();
    }
	
	/**
	 * 
	 * 生成一个视频upload Token
	 * 
	 * */
	public String createUploadVideoToken(String fileKey, long deadline){
		StringMap tokenPolicy = new StringMap();
		tokenPolicy.put("callbackUrl", uploadVideoCallbackUrl);
		String callBackBody = "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\","
				+ "\"fname\":\"$(fname)\",\"ext\":\"$(ext)\",\"fsize\":$(fsize)}";
		tokenPolicy.put("callbackBody", callBackBody);
		tokenPolicy.put("callbackBodyType", "application/json");
		tokenPolicy.put("insertOnly", 1);
		tokenPolicy.put("detectMime", 1);
		tokenPolicy.put("mimeLimit", "video/*");
		return auth.uploadTokenWithDeadline(uploadVideoBucket, fileKey, deadline, tokenPolicy, true);
	}
	
	/**
	 * 
	 * 生成一个上传图片upload Token
	 * 
	 * */
	public String createUploadImgToken(String fileKey, long deadline, boolean isPublic){
		StringMap tokenPolicy = new StringMap();
		tokenPolicy.put("callbackUrl", uploadImgCallbackUrl);
		String callBackBody = "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\","
				+ "\"fname\":\"$(fname)\",\"ext\":\"$(ext)\",\"fsize\":$(fsize)}";
		tokenPolicy.put("callbackBody", callBackBody);
		tokenPolicy.put("callbackBodyType", "application/json");
		tokenPolicy.put("insertOnly", 1);
		tokenPolicy.put("detectMime", 1);
		tokenPolicy.put("mimeLimit", "image/*");
		if (isPublic) {
			return auth.uploadTokenWithDeadline(publicImgBucket, fileKey, deadline, tokenPolicy, true);
        }
		return auth.uploadTokenWithDeadline(uploadImgBucket, fileKey, deadline, tokenPolicy, true);
	}
	
	/**
	 * 
	 * 根据上传的视频token生成一个可访问的地址, 有效期一个小时
	 * 
	 * @param url 上传的视频的存储地址
	 * 
	 * */
	public String createVideoAccessAddress(String bucket, String fileKey){
		return auth.privateDownloadUrl(uploadVideoBucketHost + "/" + fileKey);
	}
	

}
