package com.laolaiqiao.sys.web.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.codi.laolaiqiao.api.domain.UploadedVideo;
import com.codi.laolaiqiao.common.validation.MsgParams;

import lombok.Data;

@Data
public class VideoUploadTokenReq {
	
	@MsgParams(value = "标题")
	@NotNull
	@Length(min = 1, max = 15)
	public String title;
	
	
	@Min(UploadedVideo.CATEGORY_PERSONAL)
	@Max(UploadedVideo.CATEGORY_TEAM)
	public int category = UploadedVideo.CATEGORY_PERSONAL;
	
	@MsgParams(value = "用户")
	@NotNull
	@Min(0)
	public Long userId;

}
