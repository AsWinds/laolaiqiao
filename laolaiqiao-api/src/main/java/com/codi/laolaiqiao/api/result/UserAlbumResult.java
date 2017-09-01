package com.codi.laolaiqiao.api.result;

import java.math.BigInteger;

import com.codi.laolaiqiao.common.qiniu.QiNiuManager;

import lombok.Getter;

@Getter
public class UserAlbumResult {
	
	private Long albumId;
	private String name;
	private String imgUrl;
	
	public UserAlbumResult(BigInteger albumId, String name, String bucket, String fkey){
		this.albumId = albumId.longValue();
		this.name = name;
		if (bucket == null) {
			fkey = QiNiuManager.defaultUserPhoto;
		}
		this.imgUrl = QiNiuManager.getImgFileUrl(fkey);
	}

}
