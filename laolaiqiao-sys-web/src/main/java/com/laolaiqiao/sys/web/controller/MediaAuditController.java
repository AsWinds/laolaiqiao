package com.laolaiqiao.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.sys.biz.service.MediaAuditService;
import com.laolaiqiao.sys.web.result.SysResult;
import com.laolaiqiao.sys.web.security.SysUserUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(value = "/media/audit")
public class MediaAuditController {

	@Autowired
	private MediaAuditService mediaAuditService;

	@RequestMapping(value = "/videos", method = RequestMethod.GET)
	public BaseResult getNeedAuditVideos(Pageable pageable){
		return new SysResult(mediaAuditService.getNeedAuditVideoTokens(pageable));
	}

	@RequestMapping(value = "/videoAddress", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult accessVideo(@RequestParam("id") Long videoTokenId) {
	    log.debug("Audit video token : " + videoTokenId);
	    return new SysResult(mediaAuditService.getPrivateVideoAccessUrl(videoTokenId));
    }

	@RequestMapping(value = "/commitVideoAuditResult", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult commitVideoAuditResult(@RequestParam("id") Long videoTokenId, @RequestParam("isOk") Boolean isOk){
		mediaAuditService.auditVideo(videoTokenId, isOk.booleanValue(), SysUserUtil.getLoginUser().getUserId());
		return new SysResult(true);
	}

}
