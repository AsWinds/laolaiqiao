package com.codi.laolaiqiao.api.utils;

import com.codi.laolaiqiao.api.domain.UploadedVideo;
import com.codi.laolaiqiao.api.domain.VideoUploadToken;

public abstract class VideoUtil {
	
	public static String getCategoryStr(int category) {
		if (category == UploadedVideo.CATEGORY_PERSONAL) {
	        return "个人";
        }else if (category == UploadedVideo.CATEGORY_TEAM) {
        	return "团队";
        }
		throw new IllegalArgumentException("Unknown category: " + category);
    }
	
	public static String getVideoTokenStatusStr(int status) {
		if (status == VideoUploadToken.STATUS_AUDIT_PASS) {
	        return "审核通过";
        }else if (status == VideoUploadToken.STATUS_DEL) {
        	return "已删除";
        }else if (status == VideoUploadToken.STATUS_NOT_AUDIT) {
			return "审核中";
		}
		throw new IllegalArgumentException("Unknown category: " + status);
    }

}
