package com.codi.laolaiqiao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.service.UserAlbumService;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.CollectionResult;
import com.laolaiqiao.web.servlet.util.UserUtil;

@RestController
@RequestMapping("/user/album")
public class UserAlbumController {
	
	@Autowired
	UserAlbumService uaService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public BaseResult userAlbums(@RequestParam(required = false) Long userId, Pageable pageable){
		if (userId == null) {
			userId = UserUtil.getUserId();
		}
		if (userId == null) {
			return new BaseResult("1001");
		}
		return new CollectionResult<>(uaService.getUserAlbums(userId, pageable));
	}
	
	@RequestMapping(value = "/imgs", method = RequestMethod.GET)
	public BaseResult albumImgs(Long albumId, Pageable pageable){
		return new CollectionResult<>(uaService.getAlbumImgs(albumId, pageable));
	}

}
