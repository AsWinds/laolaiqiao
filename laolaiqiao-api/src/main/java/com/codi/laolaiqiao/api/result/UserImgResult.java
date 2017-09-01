package com.codi.laolaiqiao.api.result;

import com.codi.laolaiqiao.api.domain.UserImg;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;

import lombok.Getter;

@Getter
public class UserImgResult {
	
	private Long userId;
	private Long albumId;
	private String url;
	
	public UserImgResult(UserImg ui){
		this.userId = ui.getUserId();
		this.albumId = ui.getAlbumId();
		if (ui.getUploadComplete()) {
			this.url = QiNiuManager.getImgFileUrl(ui.getBucket());
		}
	}

}
