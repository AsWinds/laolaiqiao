package com.codi.laolaiqiao.sys.biz.service.impl;

import com.codi.laolaiqiao.api.domain.VideoUploadToken;
import com.codi.laolaiqiao.api.result.AuditVideoToken;
import com.codi.laolaiqiao.biz.component.QiNiuPfopManager;
import com.codi.laolaiqiao.biz.component.QiNiuUploadManager;
import com.codi.laolaiqiao.biz.dao.UploadedVideoDao;
import com.codi.laolaiqiao.biz.dao.VideoUploadTokenDao;
import com.codi.laolaiqiao.common.util.ObjectUtil;
import com.codi.laolaiqiao.common.util.TimeUtil;
import com.codi.laolaiqiao.sys.biz.service.MediaAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class MediaAuditServiceImpl implements MediaAuditService {

	@Autowired
	private VideoUploadTokenDao vutDao;

	@Autowired
	private UploadedVideoDao uvDao;

	@Autowired
	private QiNiuUploadManager uploadManager;

	@Autowired
	private QiNiuPfopManager pfopManager;

	@Transactional
	@Override
    public String getPrivateVideoAccessUrl(Long videoTokenId) {
		Assert.notNull(videoTokenId);
		VideoUploadToken token = vutDao.findOne(videoTokenId);
		Assert.notNull(token);
	    return uploadManager.createVideoAccessAddress(token.getBucket(), token.getStoredKey());
    }

	@Transactional
	@Override
    public void auditVideo(Long videoTokenId, boolean isOk, Long sysUserId) {
		Assert.notNull(videoTokenId);
		Assert.notNull(sysUserId);
	    VideoUploadToken token = vutDao.findOne(videoTokenId);
	    Assert.notNull(token);
	    Assert.isTrue(token.getUploadComplete());
	    if (isOk) {
	    	token.setStatus(VideoUploadToken.STATUS_AUDIT_PASS);
        }else {
        	token.setStatus(VideoUploadToken.STATUS_DEL);
		}
	    token.setAuditUserId(sysUserId);
	    token = vutDao.save(token);
	    //审核过后直接开始转码, 通知七牛转码, 生成缩略图
	    if (isOk) {
	    	pfopManager.transcodeAndCreateThumbnail(token);
        }
    }

	@Override
    public Page<AuditVideoToken> getNeedAuditVideoTokens(Pageable page) {
        int recordIndex = page.getPageNumber() * page.getPageSize();
        Long date = TimeUtil.currentTimeAsSeconds();

        int recordCount = vutDao.countByExpireTimeBefore(date);
        // 没有更多的数据
        if (recordCount == 0 || recordIndex > recordCount) {
            return new PageImpl<>(new ArrayList<AuditVideoToken>());
        }

		List<Object[]> objs = vutDao.findByExpireTimeBefore(date, recordIndex, page.getPageSize());
	    List<AuditVideoToken> auditVideoList =  ObjectUtil.newObjs(AuditVideoToken.class, objs);
	    return new PageImpl<AuditVideoToken>(auditVideoList, page, recordCount);
    }

}
