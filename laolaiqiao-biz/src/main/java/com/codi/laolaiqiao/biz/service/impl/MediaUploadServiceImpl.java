package com.codi.laolaiqiao.biz.service.impl;


import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.codi.laolaiqiao.api.domain.UploadedVideo;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.domain.UserAlbum;
import com.codi.laolaiqiao.api.domain.UserImg;
import com.codi.laolaiqiao.api.domain.VideoUploadToken;
import com.codi.laolaiqiao.api.domain.sys.SysImg;
import com.codi.laolaiqiao.api.req.QiNiuUploadCallback;
import com.codi.laolaiqiao.api.result.UploadTokenResult;
import com.codi.laolaiqiao.api.result.sys.ImgResult;
import com.codi.laolaiqiao.api.service.MediaUploadService;
import com.codi.laolaiqiao.biz.component.QiNiuFileManager;
import com.codi.laolaiqiao.biz.component.QiNiuPfopManager;
import com.codi.laolaiqiao.biz.component.QiNiuUploadManager;
import com.codi.laolaiqiao.biz.dao.SysImgDao;
import com.codi.laolaiqiao.biz.dao.UserAlbumDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.biz.dao.UserImgDao;
import com.codi.laolaiqiao.biz.dao.VideoUploadTokenDao;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.qiniu.NameUtil;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.codi.laolaiqiao.common.util.TimeUtil;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.qiniu.storage.model.FileInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MediaUploadServiceImpl implements MediaUploadService {
	private static final long EXPIRES = 3600;
	
	@Autowired
	private VideoUploadTokenDao vutDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SysImgDao sysImgDao;
	
	@Autowired
	private UserAlbumDao uaDao;
	
	@Autowired
	private UserImgDao uiDao;
	
	@Autowired
	private QiNiuUploadManager qiNiuManager;
	
	@Autowired
	private QiNiuPfopManager pfopManager;
	
	@Autowired
	private QiNiuFileManager fileManager;
	
	public MediaUploadServiceImpl() {
    }

	@Transactional
	@Override
    public UploadTokenResult createUploadVideoToken(Long userId, String title, int category) {
		String fileKey = userId + "_" + NameUtil.nowTimeStr();
		long deadline = TimeUtil.currentTimeAsSeconds() + EXPIRES;
		String token = qiNiuManager.createUploadVideoToken(fileKey, deadline);
		String bucket = QiNiuManager.uploadVideoBucket;
		VideoUploadToken vutToken;
		switch (category) {
		case UploadedVideo.CATEGORY_PERSONAL:
			vutToken = new VideoUploadToken(title, userId, bucket, fileKey, deadline);
			break;
		
		case UploadedVideo.CATEGORY_TEAM:
			User u = userDao.findOne(userId);
			if (!u.getIsLeader()) {
				throw new BaseException("8002");
			}
			//TODO
			vutToken = new VideoUploadToken(title, userId, u.getTeamId(), bucket, fileKey, deadline);
			break;

		default:
			throw new BaseException("9003");
		}
	    vutToken = vutDao.save(vutToken);
	    log.debug("Create VideoUploadToken {} for userId:{}, title:{}", vutToken.getId(), userId, title);
	    return new UploadTokenResult(vutToken.getStoredKey(), token);
    }

	@Transactional
	@Override
    public void handleUploadVideoCallback(QiNiuUploadCallback vuc) {
	    String bucket = vuc.bucket, key = vuc.key;
	    Assert.notNull(bucket);
	    Assert.notNull(key);
	    VideoUploadToken vut = vutDao.findByBucketAndStoredKey(bucket, key);
	    if (vut == null) {
	    	log.error("Unexpectd callback, ENV配错???, bucket:{}, key:{}", bucket, key);
	        throw new BaseException("0002");
        }
	    vut = vut.onUploadComplete(vuc);
	    //检查用户, 官方视频直接转码, 无需审核
	    User user = userDao.findOne(vut.getUserId());
	    if (User.ROLE_OFFICIAL == user.getRole().intValue()) {
	    	vut.setStatus(VideoUploadToken.STATUS_AUDIT_PASS);
	    	vut = vutDao.save(vut);
	    	pfopManager.transcodeAndCreateThumbnail(vut);
	    	return;
        }
	    //等待审核
    	vut.setStatus(VideoUploadToken.STATUS_NOT_AUDIT);
    	vut = vutDao.save(vut);
    }

	@Transactional
	@Override
    public ImgResult handleUploadImgCallback(QiNiuUploadCallback vuc) {
	    String fileKey = vuc.getKey();
	    if (NameUtil.isSysImg(fileKey)) {
	        SysImg img = sysImgDao.findByBucketAndStoredKey(vuc.bucket, fileKey);
	        img.onUploadComplete(vuc.getFname());
	        img = sysImgDao.save(img);
	        return new ImgResult(img.getBucket(), img.getStoredKey());
        }else if (NameUtil.isUserImg(fileKey)) {
			UserImg ui = uiDao.findByBucketAndStoredKey(vuc.bucket, fileKey);
			ui.onUploadComplete(vuc.getFname());
			ui = uiDao.save(ui);
			return new ImgResult(ui.getBucket(), ui.getStoredKey());
		}
	    log.error("未知的fileKey:" + fileKey);
	    throw new IllegalStateException(fileKey);
    }
	
	@Transactional
	@Override
    public BaseResult processQiniuUploadFail(String fileKey, String fname, long fsize) {
		//TODO
		String bucket = QiNiuManager.uploadVideoBucket;
	    FileInfo fi = fileManager.getFileInfo(bucket, fileKey);
	    if (fi == null) {
	        return new BaseResult("8001");
        }
	    String ext = FilenameUtils.getExtension(fname);
	    QiNiuUploadCallback cb = new QiNiuUploadCallback(fileKey, fi.hash, bucket, fname, ext, fi.fsize);
	    handleUploadVideoCallback(cb);
	    return new BaseResult(true);
    }

	@Transactional
	@Override
	public UploadTokenResult createUploadImgToken(Long userId, String album) {
		UserAlbum ua = uaDao.findByUserIdAndName(userId, album);
		if (ua == null) {
			ua = new UserAlbum(album, userId);
			ua = uaDao.save(ua);
		}
		String fileKey = NameUtil.IMG_USER_PREFIX + userId + "_" + NameUtil.nowTimeStr();
		long deadline = TimeUtil.currentTimeAsSeconds() + EXPIRES;
		String token = qiNiuManager.createUploadImgToken(fileKey, deadline, true);
		String bucket = QiNiuManager.publicImgBucket;
		UserImg img = new UserImg(userId, ua.getId(), bucket, fileKey, deadline);
		img = uiDao.save(img);
		return new UploadTokenResult(fileKey, token);
	}
	
	

}
