package com.laolaiqiao.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.biz.dao.SysImgDao;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.laolaiqiao.sys.web.result.SysResult;


@RestController
@RequestMapping("/img")
public class ImgController {
	
	@Autowired
	SysImgDao sysImgDao;
	
	@RequestMapping(value = "/sys/list", method = RequestMethod.GET)
	public BaseResult list(Pageable pageable){
		return new SysResult(sysImgDao.findAll(pageable));
	}
	

}
