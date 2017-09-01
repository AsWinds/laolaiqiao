package com.codi.laolaiqiao.api.domain;

import com.codi.laolaiqiao.common.entity.Domain;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * 上传的审核过的视频表
 *
 * UploadedVideo表的视频地址有可能是变化的, 其数据都来自VideoUploadToken数据,
 * 一般来说, 这个表只是视频转码成功后的一个备份, 未审核的视频在该表不存在
 *
 * 该表和VideoUploadToken属于一对一关系, 你可以认为该表冗余了VideoUploadToken表的数据
 *
 * */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UploadedVideo extends Domain {

    /**
     * 常量 视频类型：个人
     */
    public static final int CATEGORY_PERSONAL = 0;

    /**
     * 常量 视频类型：团队
     */
    public static final int CATEGORY_TEAM = 1;

    /**
     * 常量 视频类型数组
     */
    public static final List<Integer> CATEGORYS = Collections.unmodifiableList(Arrays.asList(CATEGORY_PERSONAL, CATEGORY_TEAM));

    /**
     * 视频名称
     */
	private String name;

    /**
     * 视频在云服务器上的URL
     */
	private String url;

    /**
     * 视频缩略图在云服务器上的URL
     */
    private String thumbnailUrl;

    /**
     * 上传视频者ID
     */
	private Long uploadUserId;

	/**
     *
     * 该视频归属的team
     *
     * */
    private Long ownerTeamId;

    /**
     * 点赞数
     */
    private Integer likeAmount;

	/**
	 *
     * 视频类型
     * 0 个人
     * 1 团队
     *
     * */
    private int category;

    /**
     * 浏览量
     */
    private Integer visitAmount;

    /**
     *
     * VideoUploadToken ID
     * */
    @Column(updatable = false, nullable = false, unique = true)
    private Long uvtId;

    public UploadedVideo(Long id, Integer likeAmount, Integer visitAmount, Integer version, String name,
                         String url, String thumbnailUrl, Long uploadUserId, Long ownerTeamId, int category) {
        setId(id);
        setVersion(version);
        this.likeAmount = likeAmount;
        this.visitAmount = visitAmount;
        this.name = name;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.uploadUserId = uploadUserId;
        this.ownerTeamId = ownerTeamId;
        this.category = category;
    }

    /**
     *
     * 创建一个UploadedVideo并指定是否使用文件本身的缩略图地址
     *
     * */
    public UploadedVideo(VideoUploadToken token, boolean useFileThumbnail){
    	if (token.getOwnerTeamId() == null) {
	        this.category = CATEGORY_PERSONAL;
        }else {
			this.category = CATEGORY_TEAM;
		}
    	this.likeAmount = 0;
    	this.name = token.getTitle();
    	this.ownerTeamId = token.getOwnerTeamId();

    	String fileKey = token.getStoredKey();
    	if (useFileThumbnail) {
    		this.thumbnailUrl = QiNiuManager.getThumbnailUrl(fileKey);
        }else {
        	this.thumbnailUrl = QiNiuManager.getImgFileUrl(QiNiuManager.defaultVideoThumbnail);
		}
    	this.uploadUserId = token.getUserId();
    	this.url = QiNiuManager.getPublicVideoUrl(fileKey);
    	this.visitAmount = 0;
    	this.uvtId = token.getId();
    }

}
