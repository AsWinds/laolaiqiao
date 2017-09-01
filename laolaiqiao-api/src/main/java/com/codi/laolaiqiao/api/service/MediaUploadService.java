package com.codi.laolaiqiao.api.service;

import com.codi.laolaiqiao.api.req.QiNiuUploadCallback;
import com.codi.laolaiqiao.api.result.UploadTokenResult;
import com.codi.laolaiqiao.api.result.sys.ImgResult;
import com.codi.laolaiqiao.common.web.result.BaseResult;

public interface MediaUploadService {

	UploadTokenResult createUploadVideoToken(Long userId, String title, int category);

	void handleUploadVideoCallback(QiNiuUploadCallback vuc);

	ImgResult handleUploadImgCallback(QiNiuUploadCallback vuc);
	
	BaseResult processQiniuUploadFail(String fileKey, String fileName, long fsize);

	UploadTokenResult createUploadImgToken(Long userId, String album);

}
