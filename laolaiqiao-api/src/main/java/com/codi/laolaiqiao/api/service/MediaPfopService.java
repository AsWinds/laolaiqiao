package com.codi.laolaiqiao.api.service;

import com.codi.laolaiqiao.api.req.QiNiuPfopCallback;

public interface MediaPfopService {
	
	/**
	 * 
	 * 源文件的bucket和storedKey
	 * 
	 * */
	void onQiNiuPfopCallBack(QiNiuPfopCallback cb);

}
