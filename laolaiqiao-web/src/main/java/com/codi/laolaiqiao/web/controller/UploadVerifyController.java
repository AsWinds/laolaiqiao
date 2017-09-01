package com.codi.laolaiqiao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.service.MediaUploadService;
import com.codi.laolaiqiao.common.web.result.BaseResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/user/upload")
public class UploadVerifyController {
	
	@Autowired
	private MediaUploadService uploadService;
	
	@RequestMapping(value = "/video/fail", method = RequestMethod.POST)
	public BaseResult qiniuReturnFail(@RequestParam("fileKey") String fileKey,
											@RequestParam("fileName") String fname, 
											@RequestParam("fsize") long fsize) {
		log.error("APP Upload file, QiNiu return Fail, key:{}, fname:{}, fsize:{}", fileKey, fname, fsize);
		return uploadService.processQiniuUploadFail(fileKey, fname, fsize);   
    }

}
