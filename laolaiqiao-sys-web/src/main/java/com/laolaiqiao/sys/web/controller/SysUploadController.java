package com.laolaiqiao.sys.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.sys.biz.service.SysMediaUploadService;
import com.laolaiqiao.sys.web.req.VideoUploadTokenReq;
import com.laolaiqiao.sys.web.result.SysResult;
import com.laolaiqiao.sys.web.security.SysUserUtil;

@RestController
@RequestMapping(value = "/sys/upload")
public class SysUploadController {
	
	@Autowired
	SysMediaUploadService uploadService;
	
	@RequestMapping(value = "/imgToken", method = RequestMethod.GET)
	public BaseResult imgUploadToken(){
		Long sysUserId = SysUserUtil.getLoginUser().getUserId();
		return new SysResult(uploadService.createSysImgUploadToken(sysUserId));
	}
	
	@RequestMapping(value = "/videoToken", method = RequestMethod.POST)
	public BaseResult videoUploadToken(@Valid VideoUploadTokenReq req){
		Long sysUserId = SysUserUtil.getLoginUser().getUserId();
		return new SysResult(uploadService.createUserVideoUploadToken(sysUserId, req.userId, req.title, req.category));
	}

}
