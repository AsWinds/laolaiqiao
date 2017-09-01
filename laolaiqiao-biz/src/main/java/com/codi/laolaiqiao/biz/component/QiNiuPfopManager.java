package com.codi.laolaiqiao.biz.component;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.codi.laolaiqiao.api.domain.VideoUploadToken;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.qiniu.common.Zone;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;

/**
 * 
 * 视频处理相关, 转码, 生成缩略图
 * 
 * */
@Slf4j
@Component
public class QiNiuPfopManager extends QiNiuManager {
	
	private OperationManager operationManager;

	public QiNiuPfopManager() {
		super();
		Configuration cfg = new Configuration(Zone.autoZone());
		operationManager = new OperationManager(auth, cfg);
	}

	/**
	 * 
	 * 转码并生成缩略图
	 * 
	 * 出错返回null, 需要注意返回值
	 * 
	 * */
	public String transcodeAndCreateThumbnail(VideoUploadToken token) {
		String srcBucket = token.getBucket();
		String fileKey = token.getStoredKey();
		String saveMp4Entry = publicVideoBucket + ":" + getPublicVideoName(fileKey);
		String saveJpgEntry = publicImgBucket + ":" + getThumbnailName(fileKey);
		String avthumbMp4Fop = String.format("avthumb/mp4|saveas/%s", UrlSafeBase64.encodeToString(saveMp4Entry));
		String vframeJpgFop = String.format("vframe/jpg/offset/10/w/230/h/130|saveas/%s", UrlSafeBase64.encodeToString(saveJpgEntry));
		//将多个数据处理指令拼接起来
		String persistentOpfs = StringUtils.join(new String[]{avthumbMp4Fop, vframeJpgFop}, ";");
		StringMap params = new StringMap()
				.putWhen("force", 1, true)
				.putNotEmpty("pipeline", dataPipeline)
				.putNotEmpty("notifyURL", transcodeAndCreateThumbnailCallbackUrl);
		try {
			//可以根据该 persistentId 查询任务处理进度
		    String persistentId = operationManager.pfop(srcBucket, fileKey, persistentOpfs, params);
		    log.info("Create transcodeAndCreateThumbnail for token:{} and get persistentId:{}", token.getId(), persistentId);
		    return persistentId;
		} catch (Exception e) {
		    log.error("QiniuException", e);
		    return null;
		}
    }
}
