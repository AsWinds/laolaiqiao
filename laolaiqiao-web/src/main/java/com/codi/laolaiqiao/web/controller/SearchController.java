package com.codi.laolaiqiao.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.result.SearchResult;
import com.codi.laolaiqiao.api.result.VideoQueryResult;
import com.codi.laolaiqiao.api.service.SearchService;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.CollectionResult;
import com.codi.laolaiqiao.web.controller.reqModel.MoreTeamReq;
import com.codi.laolaiqiao.web.controller.reqModel.MoreVideoReq;
import com.codi.laolaiqiao.web.controller.reqModel.SearchReq;

/**
 * Created by song-jj on 2017/2/9.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索视频或者艺术团
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "videoOrArtists", method = RequestMethod.GET)
    public BaseResult searchVideoOrArtists(@Valid SearchReq req) {
        // 查询数据
        List<SearchResult> searchList = searchService.search(req.keyword, req.type, req.size, req.page);
        CollectionResult<SearchResult> cr = new CollectionResult<>();
        cr.setObjs(searchList);
        return cr;
    }

    /**
     * 搜索更多视频
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "moreVideos", method = RequestMethod.GET)
    public BaseResult moreVideos(@Valid MoreVideoReq req) {
        // 查询数据
        List<VideoQueryResult> searchList = searchService.searchVideos(req.keyword, req.size, req.page);
        CollectionResult<VideoQueryResult> cr = new CollectionResult<>();
        cr.setObjs(searchList);
        return cr;
    }

    /**
     * 搜索更多艺术团
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "moreArtTeams", method = RequestMethod.GET)
    public BaseResult moreArtTeams(@Valid MoreTeamReq req) {
        // 查询数据
        List<ArtTeamSearchResult> searchList = searchService.searchTeams(req.keyword, req.size, req.page);
        CollectionResult<ArtTeamSearchResult> cr = new CollectionResult<>();
        cr.setObjs(searchList);
        return cr;
    }
}
