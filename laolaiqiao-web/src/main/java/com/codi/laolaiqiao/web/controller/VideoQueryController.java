package com.codi.laolaiqiao.web.controller;

import com.codi.laolaiqiao.api.result.VideoQueryResult;
import com.codi.laolaiqiao.api.service.UploadedVideoService;
import com.codi.laolaiqiao.common.Const;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.CollectionResult;
import com.codi.laolaiqiao.web.controller.reqModel.SearchVideoByPageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户无关的视频Controller类
 * Created by song-jj on 2017/2/27.
 */
@RestController
@RequestMapping("/video")
public class VideoQueryController {

    @Autowired
    private UploadedVideoService uploadedVideoService;

    /**
     * 加载视频，根据指定字段排序
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "searchByPage", method = RequestMethod.GET)
    public BaseResult searchByPage(@Valid SearchVideoByPageReq req) {
        // 查询数据
        Page<VideoQueryResult> videos = uploadedVideoService.queryVideoWithOrder(req.orderBy, req.page, req.size);
        CollectionResult<VideoQueryResult> cr = new CollectionResult<>(videos);
        return cr;
    }

    /**
     * 查询视频详情
     * @param page
     * @param teamId
     * @return
     */
    @RequestMapping(value = "team", method = RequestMethod.GET)
    public BaseResult team(@PageableDefault(size = Const.SEARCH_RESULT_SIZE) Pageable page, Long teamId) {
        // 查询数据
        Page<VideoQueryResult> videos = uploadedVideoService.queryTeamVideo(page, teamId);
        return new CollectionResult<>(videos);
    }

}
