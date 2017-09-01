package com.codi.laolaiqiao.api.result;

import com.codi.laolaiqiao.api.utils.VideoUtil;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.List;

/**
 * 视频查询结果
 * Created by song-jj on 2017/2/13.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoQueryResult {
    /**
     * 资源ID
     */
    private Long id;

    /**
     * 视频或者艺术团名称
     */
    private String name;

    /**
     * 视频上传者
     */
    private String userName;

    /**
     * 用户所属团队
     */
    private String teamName;

    /**
     * 点赞数量
     */
    private Integer likeAmount;

    /**
     *  浏览量
     */
    private Integer visitAmount;

    /**
     * 视频缩略图
     */
    private String thumbnailUrl;

    /**
     * 评论总数
     */
    private Long commentCount;

    /**
     * 当前用户是否对该视频点赞
     */
    private Boolean isLiked;

    /**
     * 当前用户是否收藏该视频
     */
    private Boolean isStarred;

    /**
     * 是否关注
     */
    private Boolean isFollow;

    /**
     * 视频URL地址
     */
    private String url;

    /**
     * 视频状态
     *
     * */
    private String status;

    /**
     * 上传视频的用户ID
     */
    private Long uploadUserId;

    /**
     * 评论列表
     */
    private List<VideoCommentQueryResult> comments;

    public VideoQueryResult() {
    }

    /**
     * 构造方法
     *
     * @param id       资源ID
     * @param name     视频或者艺术团名称
     * @param userName 视频上传者
     */
    public VideoQueryResult(Long id, String name, String userName, String teamName, Integer likeAmount, Integer visitAmount, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.teamName = teamName;
        this.likeAmount = likeAmount;
        this.visitAmount = visitAmount;
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * 构造方法
     * @param id       资源ID
     * @param name     视频或者艺术团名称
     * @param userName 上传者名称
     * @param likeAmount 点赞数
     * @param visitAmount 粉丝数
     * @param thumbnailUrl 视频截图
     */
    public VideoQueryResult(Long id, String name, String userName, Integer likeAmount, Integer visitAmount, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.likeAmount = likeAmount;
        this.visitAmount = visitAmount;
        this.thumbnailUrl = thumbnailUrl;
    }

    public VideoQueryResult(BigInteger id, String name, String userName, String teamName, Integer likeAmount, Integer visitAmount, String thumbnailUrl) {
    	if (id != null) {
    		this.id = id.longValue();
        }
        this.name = name;
        this.userName = userName;
        this.teamName = teamName;
        this.likeAmount = likeAmount;
        this.visitAmount = visitAmount;
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     *
     * 查询个人视频时用到
     *
     * */
    public VideoQueryResult(BigInteger id, String name, String userName, String teamName,
    		BigInteger likeAmount, BigInteger visitAmount, String thumbnailUrl, Integer status) {
    	if (id != null) {
    		this.id = id.longValue();
        }
        this.name = name;
        this.userName = userName;
        this.teamName = teamName;
        if (likeAmount != null) {
        	this.likeAmount = likeAmount.intValue();
        }
        if (visitAmount != null) {
        	this.visitAmount = visitAmount.intValue();
        }
        this.thumbnailUrl = thumbnailUrl;
        if(thumbnailUrl == null){
        	this.thumbnailUrl = QiNiuManager.getImgFileUrl(QiNiuManager.defaultVideoAudit);
        }
        this.status = VideoUtil.getVideoTokenStatusStr(status);
    }

}
