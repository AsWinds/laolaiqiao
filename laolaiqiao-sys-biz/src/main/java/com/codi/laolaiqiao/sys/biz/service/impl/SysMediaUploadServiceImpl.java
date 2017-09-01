package com.codi.laolaiqiao.sys.biz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.domain.sys.SysImg;
import com.codi.laolaiqiao.api.result.UploadTokenResult;
import com.codi.laolaiqiao.api.service.MediaUploadService;
import com.codi.laolaiqiao.biz.component.QiNiuUploadManager;
import com.codi.laolaiqiao.biz.dao.SysImgDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.qiniu.NameUtil;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.codi.laolaiqiao.common.util.TimeUtil;
import com.codi.laolaiqiao.sys.biz.service.SysMediaUploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysMediaUploadServiceImpl implements SysMediaUploadService {
	
	private static final long EXPIRES = 1200;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SysImgDao siDao;
	
	@Autowired
	private QiNiuUploadManager uploadManager;
	
	@Autowired
	private MediaUploadService userMUService;

	@Transactional
	@Override
    public UploadTokenResult createSysImgUploadToken(Long sysUserId) {
		String bucket = QiNiuManager.publicImgBucket;
	    String fileKey = NameUtil.IMG_SYS_PREFIX + System.currentTimeMillis();
	    Long deadLine = TimeUtil.currentTimeAsSeconds() + EXPIRES;
	    SysImg si = new SysImg(bucket, fileKey, deadLine);
	    si = siDao.save(si);
	    log.info("Sys-User:{} create img upload token, deadLine:{}, fileKey:{}", sysUserId, deadLine, fileKey);
	    String token = uploadManager.createUploadImgToken(fileKey, deadLine, true);
	    return new UploadTokenResult(fileKey, token);
    }

	@Transactional
	@Override
	public UploadTokenResult createUserVideoUploadToken(Long sysUserId, Long userId, String title, int category) {
		log.info("系统用户:{}为用户:{}上传标题为:{}的视频", sysUserId, userId, title);
		User u = userDao.findOne(userId);
		if (u == null) {
			throw new BaseException("1003");
		}
		return userMUService.createUploadVideoToken(userId, title, category);
	}

}
