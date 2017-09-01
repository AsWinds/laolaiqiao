package com.codi.laolaiqiao.biz.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.codi.laolaiqiao.api.domain.VideoComment;
import com.codi.laolaiqiao.api.result.VideoCommentQueryResult;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;

/**
 * 视频评论DAO
 * Created by song-jj on 2017/2/22.
 */
public interface VideoCommentDao extends BaseRepository<VideoComment, Long> {

    String SQL_SELECT_BY_VIDEO_ID_AND_USER_ID = "select new com.codi.laolaiqiao.api.result.VideoCommentQueryResult(a.comment, u.name, u.userImage, a.createdAt, a.userId) from VideoComment a, User u " +
        "where a.userId = u.id and a.isValid = true and u.isDisabled = false and a.videoId = ?1";

    /**
     * 统计评论
     * @param videoId 视频ID
     * @return 评论总数
     */
    @Query("select count(a.comment) from VideoComment a, User u " +
        "where a.userId = u.id and a.isValid = true and u.isDisabled = false and a.videoId = ?1")
    Long countByVideoIdAndUserId(Long videoId);

    /**
     * 根据视频ID查询下面的所有评论
     * @param videoId 视频ID
     * @return 评论列表
     */
    @Query(SQL_SELECT_BY_VIDEO_ID_AND_USER_ID + " order by a.createdAt desc")
    List<VideoCommentQueryResult> findByVideoIdAndUserId(Long videoId);

    /***
     *根据视频ID分页查询下面的评论
     * @param videoId
     * @param page
     * @return
     */
    @Query(SQL_SELECT_BY_VIDEO_ID_AND_USER_ID)
    Page<VideoCommentQueryResult> findByPage(Long videoId, Pageable page);

}
