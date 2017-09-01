package com.codi.laolaiqiao.sys.biz.schedule;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.codi.laolaiqiao.api.domain.VideoUploadToken;
import com.codi.laolaiqiao.api.req.QiNiuUploadCallback;
import com.codi.laolaiqiao.api.service.MediaUploadService;
import com.codi.laolaiqiao.biz.component.QiNiuFileManager;
import com.codi.laolaiqiao.biz.dao.VideoUploadTokenDao;
import com.codi.laolaiqiao.common.util.TimeUtil;
import com.qiniu.storage.model.FileInfo;

@Component
public class SyncUploadVideoTask extends AbstractTask {

	@Autowired
	VideoUploadTokenDao vutDao;
	
	@Autowired
	QiNiuFileManager fileManager;
	
	@Autowired
	MediaUploadService mediaUploadService;
	
	@PostConstruct
	public void init(){
		scheduledExecutorService.scheduleAtFixedRate(this, 5, 60, TimeUnit.SECONDS);
	}

	@Override
	protected void doBiz() {
		Long now = TimeUtil.currentTimeAsSeconds();
		Pageable pageable = new PageRequest(0, 10, Direction.ASC, "id");
		Page<Long> vutIds = vutDao.findIdByUploadCompleteAndExpireTimeBeforeAndStatus(false, now, VideoUploadToken.STATUS_NOT_AUDIT, pageable);
		for (Long vutId : vutIds) {
			checkVideoUploadComplete(vutId);
		}
	}
	
	private void checkVideoUploadComplete(Long vutId){
		try {
			transactionTemplate.execute(new TransactionCallback<Void>() {

				@Override
				public Void doInTransaction(TransactionStatus status) {
					VideoUploadToken vut = vutDao.findOne(vutId);
					String fkey = vut.getStoredKey();
					String bucket = vut.getBucket();
					FileInfo fi = fileManager.getFileInfo(bucket, fkey);
					if (fi == null) {
						log.info("忽略 VideoUploadToken, id:" + vutId + " 因为文件确实上传失败!");
						vut.setStatus(VideoUploadToken.STATUS_DEL);
						vut = vutDao.save(vut);
						return null;
					}
					log.info("处理VideoUploadToken, id:{}, 因为文件确实上传成功", vutId);
					QiNiuUploadCallback cb = new QiNiuUploadCallback(fkey, fi.hash, bucket, null, fi.mimeType, fi.fsize);
					mediaUploadService.handleUploadVideoCallback(cb);
					return null;
				}
			});
		} catch (Exception e) {
			log.error("Check VUT_ID:" + vutId + " fail!", e);
		}
	}


}
