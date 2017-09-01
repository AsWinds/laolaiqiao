package com.codi.laolaiqiao.api.result;

import java.math.BigInteger;
import java.util.Date;

import com.codi.base.util.DateUtils;
import com.codi.laolaiqiao.api.domain.UploadedVideo;
import com.codi.laolaiqiao.api.utils.VideoUtil;


public class AuditVideoToken {
	
	public final Long tokenId;
	
	public final String title; 
	
	public final String uploadUser;
	
	public final String category;
	
	public final String status;
	
	public final String uploadTime;
	
	public AuditVideoToken(BigInteger tokenId, String title, String uploadUser, BigInteger ownerTeamId, Integer status, Date uploadTime) {
		this.tokenId = tokenId.longValue();
		this.title = title;
		this.uploadUser = uploadUser;
		if (ownerTeamId == null) {
			this.category = VideoUtil.getCategoryStr(UploadedVideo.CATEGORY_PERSONAL);
        }else {
        	this.category = VideoUtil.getCategoryStr(UploadedVideo.CATEGORY_TEAM);
		}
		
		this.status = VideoUtil.getVideoTokenStatusStr(status);
		this.uploadTime = DateUtils.formatDateTime(uploadTime);
    }

}
