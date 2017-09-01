package com.codi.laolaiqiao.api.result.sys;

import com.codi.laolaiqiao.common.qiniu.QiNiuManager;


public class ImgResult {
	
	public final String url;
	
	public ImgResult(String bucket, String key) {
	    this.url = QiNiuManager.getImgFileUrl(key);
    }

}
