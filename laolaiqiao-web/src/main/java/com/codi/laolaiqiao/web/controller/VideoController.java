package com.codi.laolaiqiao.web.controller;

import com.codi.laolaiqiao.api.result.LikeAndStarClickResult;
import com.codi.laolaiqiao.api.result.VideoCommentQueryResult;
import com.codi.laolaiqiao.api.result.VideoQueryResult;
import com.codi.laolaiqiao.api.service.UploadedVideoService;
import com.codi.laolaiqiao.common.Const;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.CollectionResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;
import com.codi.laolaiqiao.web.controller.reqModel.LikeOrStarCancelReq;
import com.codi.laolaiqiao.web.controller.reqModel.LikeOrStarReq;
import com.codi.laolaiqiao.web.controller.reqModel.VideoCommentReq;
import com.codi.laolaiqiao.web.controller.reqModel.VideoDetailReq;
import com.laolaiqiao.web.servlet.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 需要用户登录的视频相关Controller
 * Created by song-jj on 2017/2/13.
 */
@RestController
@RequestMapping("/user/video")
public class VideoController {

    @Autowired
    private UploadedVideoService uploadedVideoService;

    /**
     * 对视频进行评论
     * @param req
     * @return
     */
    @RequestMapping(value = "comment", method = RequestMethod.POST)
    public BaseResult comment(@Valid VideoCommentReq req) {
        // 保存
        VideoCommentQueryResult commentQueryResult = uploadedVideoService.saveComment(req.getVideoId(), UserUtil.getUserId(), req.getComment());

        return new SingleDataResult<>(commentQueryResult);
    }

    /**
     * 对视频进行点赞/收藏
     * @param req
     * @return
     */
    @RequestMapping(value = "likeOrStar", method = RequestMethod.POST)
    public BaseResult likeOrStar(@Valid LikeOrStarReq req) {
        // 保存
        uploadedVideoService.saveLikeOrStar(req.videoId, UserUtil.getUserId(), req.type);

        return new SingleDataResult<>(new LikeAndStarClickResult(true));
    }

    /**
     * 对视频 取消点赞/收藏
     * @param req
     * @return
     */
    @RequestMapping(value = "cancelLikeOrStar", method = RequestMethod.POST)
    public BaseResult cancelLikeOrStar(@Valid LikeOrStarCancelReq req) {
        // 保存
        uploadedVideoService.cancelLikeOrStar(req.videoId, UserUtil.getUserId(), req.type);

        return new SingleDataResult<>(new LikeAndStarClickResult(false));
    }

    /**
     * 查询视频详情
     * @param page
     * @return
     */
    @RequestMapping(value = "my", method = RequestMethod.GET)
    public BaseResult my(Pageable page, Long otherUserId) {
        // 查询数据
        Page<VideoQueryResult> videos = uploadedVideoService.queryVideos(page, UserUtil.getUserId(), otherUserId);
        return new CollectionResult<>(videos);
    }

    /**
     * 查询视频详情
     * @param page
     * @return
     */
    @RequestMapping(value = "myStarred", method = RequestMethod.GET)
    public BaseResult myStarred(Pageable page) {
        // 查询数据
        Page<VideoQueryResult> videos = uploadedVideoService.queryStar(page, UserUtil.getUserId());
        return new CollectionResult<>(videos);
    }

    /**
     * 查询视频详情
     * @param req
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public BaseResult detail(@Valid VideoDetailReq req) {
        // 查询数据
        VideoQueryResult video = uploadedVideoService.queryVideoDetail(req.videoId, UserUtil.getUserId(), req.size, req.page);
        return new SingleDataResult<>(video);
    }


}
