package com.codi.laolaiqiao.api.result;

import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.codi.laolaiqiao.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 评论查询结果
 * Created by song-jj on 2017/2/22.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VideoCommentQueryResult {

    /**
     * 评论时间
     */
    private String commentTime;

    /**
     * 用户头像
     */
    private String userImage;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 构造函数
     * @param comment 评论
     * @param userName 用户名
     * @param userImage 用户头像
     * @param createAt 创建时间
     * @param userId 用户ID
     */
    public VideoCommentQueryResult(String comment, String userName, String userImage, Date createAt, Long userId) {
        this.comment = comment;
        this.userImage = QiNiuManager.getImgFileUrl(userImage);
        this.userName = userName;
        this.commentTime = DateUtil.dateToString(createAt);
        this.userId = userId;
    }
}
