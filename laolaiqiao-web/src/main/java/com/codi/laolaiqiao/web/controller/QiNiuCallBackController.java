package com.codi.laolaiqiao.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.codi.laolaiqiao.api.req.QiNiuPfopCallback;
import com.codi.laolaiqiao.api.req.QiNiuUploadCallback;
import com.codi.laolaiqiao.api.result.sys.ImgResult;
import com.codi.laolaiqiao.api.service.MediaPfopService;
import com.codi.laolaiqiao.api.service.MediaUploadService;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/qiniu/callback")
public class QiNiuCallBackController {
	
	@Autowired
	private MediaUploadService mediaUploadService;
	
	@Autowired
	private MediaPfopService mediaPfopService;
	
	@RequestMapping(
			value = "/uploadVideoComplete", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			method = RequestMethod.POST
	)
	public BaseResult videoUploadComplete(@Valid @RequestBody QiNiuUploadCallback vuc){
		log.debug("QiNiu VideoUploadComplete Callback : " + JSON.toJSONString(vuc));
		mediaUploadService.handleUploadVideoCallback(vuc);
		return new BaseResult(true);
	}
	
	@RequestMapping(
			value = "/transcodeAndCreateThumbnailCallback",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST
	)
	public BaseResult transcodeAndCreateThumbnailCallback(@Valid @RequestBody QiNiuPfopCallback cb){
		log.debug("QiNiu QiNiuPfopCallback Callback : " + JSON.toJSONString(cb, true));
		mediaPfopService.onQiNiuPfopCallBack(cb);
		return new BaseResult(true);
	}
	
	@RequestMapping(
			value = "/uploadImgComplete", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			method = RequestMethod.POST
	)
	public BaseResult imgUploadComplete(@Valid @RequestBody QiNiuUploadCallback vuc){
		log.debug("QiNiu ImgUploadComplete Callback : " + JSON.toJSONString(vuc));
		ImgResult result = mediaUploadService.handleUploadImgCallback(vuc);
		return new SingleDataResult<>(result);
	}

}
