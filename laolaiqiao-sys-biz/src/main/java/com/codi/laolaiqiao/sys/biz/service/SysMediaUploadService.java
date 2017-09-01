package com.codi.laolaiqiao.sys.biz.service;

import com.codi.laolaiqiao.api.result.UploadTokenResult;

public interface SysMediaUploadService {
	
	UploadTokenResult createSysImgUploadToken(Long sysUserId);

	UploadTokenResult createUserVideoUploadToken(Long sysUserId, Long userId, String title, int category);

}
