package com.codi.laolaiqiao.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.result.UploadTokenResult;
import com.codi.laolaiqiao.api.service.MediaUploadService;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;
import com.codi.laolaiqiao.web.controller.reqModel.GetVideoUploadTokenReq;
import com.laolaiqiao.web.servlet.util.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user/upload/")
public class UserUploadController {
	
	@Autowired
	private MediaUploadService mediaUploadService;
	
	@RequestMapping(value = "/video/token", method = RequestMethod.POST)
	public BaseResult uploadVideoToken(@Valid GetVideoUploadTokenReq req){
		log.debug("UserId : {}, Upload File: {}", UserUtil.getUserId(), req.title);
		UploadTokenResult result = mediaUploadService.createUploadVideoToken(UserUtil.getUserId(), req.title, req.category);
		return new SingleDataResult<>(result);
	}
	
	@RequestMapping(value = "/img/token", method = RequestMethod.POST)
	public BaseResult uploadImgToken(String album){
		album = album.trim();
		if (album.length() > 10 || album.length() == 0) {
			throw new BaseException("1100");
		}
		log.debug("UserId : {}, Upload Imgage: {}", UserUtil.getUserId(), album);
		UploadTokenResult result = mediaUploadService.createUploadImgToken(UserUtil.getUserId(), album);
		return new SingleDataResult<>(result);
	}

}
