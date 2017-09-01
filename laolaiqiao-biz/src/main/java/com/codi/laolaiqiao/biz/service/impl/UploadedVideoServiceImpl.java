package com.codi.laolaiqiao.biz.service.impl;

import com.codi.laolaiqiao.api.domain.*;
import com.codi.laolaiqiao.api.result.LikeAndStarQueryResult;
import com.codi.laolaiqiao.api.result.VideoCommentQueryResult;
import com.codi.laolaiqiao.api.result.VideoQueryResult;
import com.codi.laolaiqiao.api.service.UploadedVideoService;
import com.codi.laolaiqiao.biz.dao.*;
import com.codi.laolaiqiao.common.Const;
import com.codi.laolaiqiao.common.ErrorConst;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.sensitiveword.SensitivewordFilter;
import com.codi.laolaiqiao.common.util.ObjectUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 视频Service
 */
@Service
@Transactional
public class UploadedVideoServiceImpl implements UploadedVideoService {

    /**
     * 点赞
     */
    private static final String TYPE_LIKE = "0";

    /**
     * 收藏
     */
    private static final String TYPE_STAR = "1";

    @Autowired
    private UploadedVideoDao uploadedVideoDao;

    @Autowired
    private VideoCommentDao videoCommentDao;

    @Autowired
    private LikeStarDao likeStarDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ArtTeamDao artTeamDao;

    @Autowired
    private FansDao fansDao;

    /**
     * 根据排序条件查询视频
     * @param orderBy 排序条件
     * @param pageIndex 第几页
     * @param pageSize 一页的数量
     * @return 排序好的视频
     */
    @Override
    public Page<VideoQueryResult> queryVideoWithOrder(String orderBy, Integer pageIndex, Integer pageSize) {
        if (!"createdAt".equals(orderBy) && !"likeAmount".equals(orderBy)) {
            throw new BaseException(ErrorConst.ERROR_ORDER_BY_NOT_FOUND);
        }

        // 参数
        pageSize = pageSize == null ? 10 : pageSize;// 默认显示数量
        pageIndex = pageIndex == null ? Const.DEFAULT_PAGE_INDEX : (pageIndex - 1);

        // 分页条件
        Sort sort = new Sort(Sort.Direction.DESC, orderBy);
        Pageable page = new PageRequest(pageIndex, pageSize, sort);

        // 查询
        Page<VideoQueryResult> videos = uploadedVideoDao.findByPage(page);

        return videos;
    }

    /**
     * 根据视频ID查询视频详情
     * @param videoId
     * @return
     */
    @Override
    public VideoQueryResult queryVideoDetail(Long videoId, Long userId, Integer pageSize, Integer pageIndex) {

        // 视频
        UploadedVideo video = uploadedVideoDao.findOne(videoId);

        // 视频未找到
        if (video == null) {
            throw new BaseException(ErrorConst.ERROR_VIDEO_NOT_FOUND);
        }

        // 视频详情
        VideoQueryResult result = new VideoQueryResult();

        // 拷贝值
        BeanUtils.copyProperties(video, result);

        // 个人视频
        Long teamId = null;
        if (UploadedVideo.CATEGORY_PERSONAL == video.getCategory()) {
            // 如果该视频是个人视频，则查询用户信息
            User user = userDao.findOne(video.getUploadUserId());
            if (user.getTeamId() != null) {
                teamId = user.getTeamId();
            }
            result.setUserName(user.getName());
        } else {
            teamId = video.getOwnerTeamId();
        }

        // 查询团队
        if (teamId != null) {
            ArtTeam team = artTeamDao.findOne(teamId);
            result.setTeamName(team.getName());
        }

        List<LikeStar> likeStars = null;

        // 是否点赞和收藏
        boolean isLiked = false;
        boolean isStarred = false;
        boolean isFollow = false;

        // 用户登录的情况下，查询点赞和收藏
        if (userId != null) {
            // 查询点赞和收藏
            likeStars = likeStarDao.findByVideoIdAndUserId(videoId, userId);

            // 查询是否关注该用户或者团队
            if (UploadedVideo.CATEGORY_PERSONAL == video.getCategory()) {
                isFollow = fansDao.countByFansIdAndUserId(userId, video.getUploadUserId()) > 0;

                // 是否关注该团队
            } else if (UploadedVideo.CATEGORY_TEAM == video.getCategory()) {
                isFollow = fansDao.countByFansIdAndTeamId(userId, video.getOwnerTeamId()) > 0;
            }

        }

        // 有点赞或者收藏
        if (!CollectionUtils.isEmpty(likeStars)) {
            for (LikeStar likeStar : likeStars) {
                // 用户赞了该视频
                if (TYPE_LIKE.equals(likeStar.getType())) {
                    isLiked = true;
                    // 用户收藏了该视频
                } else if (TYPE_STAR.equals(likeStar.getType())) {
                    isStarred = true;
                }
            }
        }
        result.setIsLiked(isLiked);// 点赞
        result.setIsStarred(isStarred);// 收藏
        result.setIsFollow(isFollow); // 关注

        // 不分页查询
        if (pageIndex == null)  {
            queryCommentWithoutPage(videoId, result);
            // 分页查询
        } else {
            queryCommentWithPage(videoId, pageSize, pageIndex, result);
        }

        // 更新视频浏览量
        updateVisitAmount(videoId,  1);

        return result;
    }

    /**
     * 分页查询视频评论
     * @param videoId
     * @param pageSize
     * @param pageIndex
     * @param result
     */
    private void queryCommentWithPage(Long videoId, Integer pageSize, Integer pageIndex, VideoQueryResult result) {
        pageSize = pageSize == null ? Const.SEARCH_RESULT_SIZE : pageSize;// 默认显示数量
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable page = new PageRequest(pageIndex - 1, pageSize, sort);

        // 分页查询
        Page<VideoCommentQueryResult> pagedComments = videoCommentDao.findByPage(videoId, page);

        // 总数量
        result.setComments(pagedComments.getContent());
        result.setCommentCount(pagedComments.getTotalElements());
    }

    /**
     * 无分页查询视频评论
     * @param videoId
     * @param result
     */
    private void queryCommentWithoutPage(Long videoId, VideoQueryResult result) {
        // 评论总数
        Long commentCount = videoCommentDao.countByVideoIdAndUserId(videoId);
        result.setCommentCount(commentCount);

        //  0条的场合，处理终止，返回数据给客户端
        if (commentCount == 0) {
            return;
        }

        // 评论列表
        List<VideoCommentQueryResult> comments = videoCommentDao.findByVideoIdAndUserId(videoId);
        result.setComments(comments);
    }

    /**
     * 保存评论
     * @param videoId
     * @param userId
     * @param comment
     * @return
     */
    @Override
    public VideoCommentQueryResult saveComment(Long videoId, Long userId, String comment) {
        // 对comment进行黄赌毒恐暴的check
        SensitivewordFilter filter = SensitivewordFilter.getInstance();
        // 如果有不当言论
        if (filter.isContaintSensitiveWord(comment, SensitivewordFilter.minMatchTYpe)) {
            throw new BaseException(ErrorConst.ERROR_COMMMENT_INVALID);
        }

        // 保存评论
        VideoComment videoComment = new VideoComment();
        videoComment.setUserId(userId);
        videoComment.setComment(comment);
        videoComment.setIsValid(true);
        videoComment.setVideoId(videoId);
        videoCommentDao.save(videoComment);

        // 查询用户信息 返回给客户端
        User user = userDao.findOne(userId);
        if (user == null) {
            throw new BaseException(ErrorConst.ERROR_USER_NOT_LOGIN);
        }

        return new VideoCommentQueryResult(comment, user.getName(), user.getUserImage(), videoComment.getCreatedAt(), userId);
    }

    /**
     * 用户对视频进行点赞、收藏
     * @param videoId
     * @param userId
     * @param type
     */
    @Override
    public void saveLikeOrStar(Long videoId, Long userId, String type) {
        checkType(type);

        // 视频不存在
        if (uploadedVideoDao.findOne(videoId) == null) {
            throw new BaseException(ErrorConst.ERROR_VIDEO_NOT_FOUND);
        }
        // 插入之前，查一把
        if (likeStarDao.count(videoId, userId, type) > 0) {
            throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
        }

        // 插入点赞收藏数据
        LikeStar likeStar = new LikeStar();
        likeStar.setVideoId(videoId);
        likeStar.setUserId(userId);
        likeStar.setType(type);
        likeStarDao.save(likeStar);

        // 收藏的情况，不用更新数量
        if (TYPE_STAR.equals(type)) {
            return;
        }

        // 更新点赞数量
        updateLikeAmount(videoId, 1);

    }

    /**
     * 检查类型
     * @param type
     */
    private boolean checkType(String type) {
        // 非法请求
        if (TYPE_STAR.equals(type) || TYPE_LIKE.equals(type)) {
            return true;
        }
        throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
    }

    /**
     * 用户对视频进行取消点赞、取消收藏
     * @param videoId
     * @param userId
     * @param type
     */
    @Override
    public void cancelLikeOrStar(Long videoId, Long userId, String type) {
        checkType(type);

        // 视频不存在
        if (uploadedVideoDao.findOne(videoId) == null) {
            throw new BaseException(ErrorConst.ERROR_VIDEO_NOT_FOUND);
        }

        // 删除之前，查一把
        if (likeStarDao.count(videoId, userId, type) != 1) {
            throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
        }

        // 删除点赞收藏数据
        likeStarDao.deleteLikeOrStar(videoId, userId, type);

        // 收藏的情况，不用更新数量
        if (TYPE_STAR.equals(type)) {
            return;
        }
        // 更新点赞数量
        updateLikeAmount(videoId, -1);

    }

    /**
     * 更新对视频的点赞数量
     * @param videoId 视频ID
     * @param amount 1 或者 -1
     */
    private void updateLikeAmount(Long videoId, Integer amount) {
        // 锁表
        UploadedVideo video = uploadedVideoDao.findOneForUpdate(videoId);

        // 未找到视频
        if (video == null) {
            throw new BaseException(ErrorConst.ERROR_VIDEO_NOT_FOUND);
        }

        uploadedVideoDao.updateLikeAmount(video.getId(), amount, new Date());
    }

    /**
     * 更新对视频的点赞数量或者收藏数量
     * @param videoId 视频ID
     * @param amount 1 或者 -1
     */
    private void updateVisitAmount(Long videoId, Integer amount) {
        // 锁表
        UploadedVideo video = uploadedVideoDao.findOneForUpdate(videoId);

        // 未找到视频
        if (video == null) {
            throw new BaseException(ErrorConst.ERROR_VIDEO_NOT_FOUND);
        }

        uploadedVideoDao.updateVisitAmount(video.getId(), amount, new Date());
    }

    /**
     * 根据视频ID和用户ID 查询 用户对视频的点赞和收藏情况
     * @param videoId
     * @param userId
     * @return
     */
    @Override
    public LikeAndStarQueryResult queryLikeAndStar(Long videoId, Long userId) {
        LikeAndStarQueryResult result = new LikeAndStarQueryResult();

        return result;
    }

    /**
     * 查询视频
     * @param page
     * @param otherUserId
     * @param userId
     * @return result
     */
    @Override
    public Page<VideoQueryResult> queryVideos(Pageable page, Long userId, Long otherUserId) {
        int recordCount;
        // 自己的主页
        if (otherUserId == null) {
            recordCount = uploadedVideoDao.countAllByUserId(userId);
            // 他人主页
        } else {
            recordCount = uploadedVideoDao.countAuditedByUserId(otherUserId);
        }

        int recordIndex = page.getPageNumber() * page.getPageSize();
        // 没有找到记录，或者超过最后一页
        if (recordCount <= 0 && recordIndex > recordCount) {
            return new PageImpl<>(new ArrayList<VideoQueryResult>(), page, recordCount);
        }

        List<VideoQueryResult> videos;
        // 自己的主页
        if (otherUserId == null) {
            videos = ObjectUtil.newObjs(VideoQueryResult.class, uploadedVideoDao.findAllByUserId(userId, recordIndex, page.getPageSize()));
            // 他人主页
        } else {
            videos = ObjectUtil.newObjs(VideoQueryResult.class, uploadedVideoDao.findAuditedByUserId(otherUserId, recordIndex, page.getPageSize()));
        }
        return new PageImpl<>(videos, page, recordCount);
    }

    /**
     * 查询我收藏的视频
     * @param page
     * @param userId
     */
    @Override
    public Page<VideoQueryResult> queryStar(Pageable page, Long userId) {
        int recordCount = likeStarDao.countByUserId(userId);

        int recordIndex = page.getPageNumber() * page.getPageSize();
        // 没有找到记录，或者超过最后一页
        if (recordCount <= 0 && recordIndex > recordCount) {
            return new PageImpl<>(new ArrayList<VideoQueryResult>(), page, recordCount);
        }

        List<VideoQueryResult> pagedVideos = ObjectUtil.newObjs(VideoQueryResult.class, likeStarDao.findByUserId(userId, recordIndex, page.getPageSize()));

        return new PageImpl<>(pagedVideos, page, recordCount);
    }

    /**
     * 查询团队视频
     *
     * @param pageable
     * @param teamId
     * @return
     */
    @Override
    public Page<VideoQueryResult> queryTeamVideo(Pageable pageable, Long teamId) {
        if (teamId == null) {
            throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
        }
        return uploadedVideoDao.findTeamVideo(teamId, pageable);
    }
}
