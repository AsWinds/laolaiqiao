package com.codi.laolaiqiao.biz.service.impl;

import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.codi.base.util.Assert;
import com.codi.laolaiqiao.api.domain.UploadedVideo;
import com.codi.laolaiqiao.api.domain.VideoUploadToken;
import com.codi.laolaiqiao.api.req.QiNiuPfopCallback;
import com.codi.laolaiqiao.api.req.QiNiuPfopCallbackItem;
import com.codi.laolaiqiao.api.service.MediaPfopService;
import com.codi.laolaiqiao.biz.dao.UploadedVideoDao;
import com.codi.laolaiqiao.biz.dao.VideoUploadTokenDao;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MediaPfopServiceImpl implements MediaPfopService {
	
	@Autowired
	private VideoUploadTokenDao vutDao;
	
	@Autowired
	private UploadedVideoDao uvDao;

	/**
	 * 
	 * bucket, key都必须有
	 * 
	 * */
	@Transactional
	@Override
	public void onQiNiuPfopCallBack(QiNiuPfopCallback cb) {
		String key = cb.inputKey, bucket = cb.inputBucket;
		boolean avthumb_success = false, vframe_success = false;
		if (cb.items != null) {
	        for (QiNiuPfopCallbackItem item : cb.items) {
            	if (item.cmd.startsWith("avthumb")) {
    	            if (StringUtils.isBlank(item.error)) {
    	                avthumb_success = true;
    	                continue;
                    }
    	            log.error("QiNiu pfop fail, id:{}, bucket:{}, key:{}, error:{}", cb.id, bucket, key, JSON.toJSON(item));
                }else if (item.cmd.startsWith("vframe")) {
    	            if (StringUtils.isBlank(item.error)) {
    	            	vframe_success = true;
    	                continue;
                    }
    	            log.error("QiNiu pfop fail, id:{}, bucket:{}, key:{}, error:{}", cb.id, bucket, key, JSON.toJSON(item));
				}else {
					throw new RuntimeException("Unkow QiNiu Pfop cmd:" + item.cmd);
				}
            }
        }
		if (!avthumb_success) {
			//TODO 视频转码失败, 啥也干不了, 查原因吧
			log.error("Unable create UploadedVideo because avthumb fail!!!");
	        return;
        }
		VideoUploadToken vut = vutDao.findByBucketAndStoredKey(bucket, key);
		if (vut == null) {
	        log.error("VideoUploadToken not found by storedKey:" + key);
	        return;
        }
		Assert.isTrue(Objects.equals(vut.getStatus(), VideoUploadToken.STATUS_AUDIT_PASS));
		UploadedVideo uv = uvDao.findByUvtId(vut.getId());
		if (uv == null) {
	        uv = new UploadedVideo(vut, vframe_success);
	        uv = uvDao.save(uv);
	        log.debug("Create UploadedVideo id:{}, for VideoUploadToken id:{}", uv.getId(), vut.getId());
	        return;
        }
		//转码生成缩略图之后又来???更新即可!!!
		String fileKey = vut.getStoredKey();
		log.debug("Update UploadedVideo URL and ThumbnailUrl, fileKey:" + fileKey);
		if (vframe_success) {
			uv.setThumbnailUrl(QiNiuManager.getThumbnailUrl(fileKey));
        }
		uv.setUrl(QiNiuManager.getPublicVideoUrl(fileKey));
		uv = uvDao.save(uv);
	}

}
