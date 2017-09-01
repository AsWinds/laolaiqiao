package com.codi.laolaiqiao.common.qiniu;

import org.springframework.util.Assert;

import com.codi.base.config.ConfigurationMgr;
import com.qiniu.util.Auth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class QiNiuManager {
	
	public static final String accessKey;
	public static final String secretKey;
	
	public static final String uploadVideoBucket;
	public static final String uploadVideoCallbackUrl;
	public static final String uploadVideoBucketHost;
	
	public static final String publicVideoBucket;
	public static final String transcodeAndCreateThumbnailCallbackUrl;
	public static final String publicVideoBucketHost;
	
	public static final String publicImgBucket;
	public static final String publicImgBucketHost;
	public static final String uploadImgCallbackUrl;
	public static final String uploadImgBucket;
	
	public static final String dataPipeline;
	
	public static final String defaultUserPhoto;
	
	public static final String defaultVideoThumbnail;
	
	public static final String defaultVideoAudit;
	
	public static final String defaultActivityPhoto;
	
	public static final String defaultTeamPhoto;
	
	public static final Auth auth;
	
	static{
		ConfigurationMgr mgr = ConfigurationMgr.getInstance();
		
		accessKey = loadConf(mgr, "qiniu.access_key");
		
		secretKey = loadConf(mgr, "qiniu.secret_key");
		
		uploadVideoBucket = loadConf(mgr, "qiniu.upload_video_bucket");
		
		uploadVideoCallbackUrl = loadConf(mgr, "qiniu.upload_video.callback_url");
		
		uploadVideoBucketHost = loadConf(mgr, "qiniu.upload_video_bucket_host");
		
		publicVideoBucket = loadConf(mgr, "qiniu.public_video_bucket");
		
		transcodeAndCreateThumbnailCallbackUrl = loadConf(mgr, "qiniu.transcodeAndCreateThumbnailCallbackUrl");
		
		publicVideoBucketHost = loadConf(mgr, "qiniu.public_video_bucket_host");
		
		publicImgBucket = loadConf(mgr, "qiniu.public_img_bucket");
		
		publicImgBucketHost = loadConf(mgr, "qiniu.public_img_bucket_host");
		
		uploadImgCallbackUrl = loadConf(mgr, "qiniu.upload_img.callback_url");
		
		uploadImgBucket = loadConf(mgr, "qiniu.upload_img_bucket");
		
		dataPipeline = loadConf(mgr, "qiniu.data_pipeline");
		
		defaultUserPhoto = loadConf(mgr, "user.default_user_photo");
		
		defaultVideoThumbnail = loadConf(mgr, "video.default_video_thumbnail");
		
		defaultVideoAudit = loadConf(mgr, "video.default_video_audit");
		
		defaultActivityPhoto = loadConf(mgr, "activity.default_activity_photo");
		
		defaultTeamPhoto = loadConf(mgr, "team.default_team_photo");
		
		auth = Auth.create(accessKey, secretKey);
	}
	
	private static String loadConf(ConfigurationMgr mgr, String key){
		String val = mgr.getString(key);
		log.debug("Load QiNiu " + key + " : " + val);
		Assert.notNull(val);
		return val;
	}
	
	public static String getThumbnailName(String fileKey){
		return "thumbnail_" + fileKey + ".jpg";
	}
	
	public static String getPublicVideoName(String fileKey){
		return fileKey + ".mp4";
	}
	
	public static String getThumbnailUrl(String fileKey){
		return publicImgBucketHost + "/" + getThumbnailName(fileKey);
	}
	
	public static String getPublicVideoUrl(String fileKey){
		return publicVideoBucketHost + "/" + getPublicVideoName(fileKey);
	}
	
	public static String getImgFileUrl(String fileName){
		return publicImgBucketHost + "/" + fileName;
	}


}
