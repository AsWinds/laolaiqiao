package com.codi.laolaiqiao.api.service;

import com.codi.laolaiqiao.api.result.LikeAndStarQueryResult;
import com.codi.laolaiqiao.api.result.VideoCommentQueryResult;
import com.codi.laolaiqiao.api.result.VideoQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 视频相关的接口服务
 */
public interface UploadedVideoService {

    /**
     * 根据排序条件分页查询视频
     * @param orderBy
     * @param pageIndex
     * @param pageSize
     * @return 排序后的视频列表
     */
    Page<VideoQueryResult> queryVideoWithOrder(String orderBy, Integer pageIndex, Integer pageSize);

    /**
     * 根据视频ID查询视频详情
     * @param videoId
     * @return
     */
    VideoQueryResult queryVideoDetail(Long videoId, Long userId, Integer pageSize, Integer pageIndex);

    /**
     * 根据视频ID和用户ID 查询 用户对视频的点赞和收藏情况
     * @param videoId
     * @param userId
     * @return
     */
    LikeAndStarQueryResult queryLikeAndStar(Long videoId, Long userId);

    /**
     * 保存用户对视频的评论
     * @param videoId
     * @param userId
     * @param comment
     */
    VideoCommentQueryResult saveComment(Long videoId, Long userId, String comment);

    /**
     * 用户对视频进行点赞、收藏
     * @param videoId
     * @param userId
     * @param type
     */
    void saveLikeOrStar(Long videoId, Long userId, String type);

    /**
     * 用户对视频进行取消点赞、取消收藏
     * @param videoId
     * @param userId
     * @param type
     */
    void cancelLikeOrStar(Long videoId, Long userId, String type);

    /**
     * 查询视频
     * @param page
     * @param otherUserId
     * @param userId
     * @return result
     */
    Page<VideoQueryResult> queryVideos(Pageable page, Long userId, Long otherUserId);

    /**
     * 查询我收藏的视频
     * @param page
     * @param userId
     * @return result
     */
    Page<VideoQueryResult> queryStar(Pageable page, Long userId);

    /**
     * 查询团队视频
     * @param pageable
     * @param teamId
     * @return
     */
    Page<VideoQueryResult> queryTeamVideo(Pageable pageable, Long teamId);
}
