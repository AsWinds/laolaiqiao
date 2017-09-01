package com.codi.laolaiqiao.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.result.SearchResult;
import com.codi.laolaiqiao.api.result.VideoQueryResult;
import com.codi.laolaiqiao.api.service.SearchService;
import com.codi.laolaiqiao.biz.dao.ArtTeamDao;
import com.codi.laolaiqiao.biz.dao.UploadedVideoDao;
import com.codi.laolaiqiao.common.Const;

/**
 * Created by song-jj on 2017/2/9.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private final String TYPE_VIDEO = "0";

    @Autowired
    private UploadedVideoDao uploadedVideoDao;

    @Autowired
    private ArtTeamDao artTeamDao;

    @Override
    public List<SearchResult> search(String keyword, String type, Integer pageSize, Integer pageIndex) {
        List<SearchResult> results;

        pageSize = pageSize == null ? Const.SEARCH_RESULT_SIZE : pageSize;// 默认显示数量
        pageIndex = pageIndex == null ? Const.DEFAULT_PAGE_INDEX : (pageIndex - 1);

        // 查询数据
        // 类型是视频的场合，查询表t_uploaded_video，只查询团队视频
        Sort sort = new Sort(Sort.Direction.DESC, "likeAmount", "createdAt");
        Pageable page = new PageRequest(pageIndex, pageSize, sort);
        if (TYPE_VIDEO.equals(type)) {
            results = uploadedVideoDao.findByNameContaining(keyword.toLowerCase(), page).getContent();
            return results;
        }

        // 类型时艺术团的场合，查询艺术团t_art_team
        sort = new Sort(Sort.Direction.DESC, "fansAmount", "createdAt");
        page = new PageRequest(0, pageSize, sort);
        results = artTeamDao.findByNameContaining(keyword.toLowerCase(), page).getContent();

        return results;
    }

    /**
     * 搜索更多视频
     *
     * @param keyword
     * @param pageSize
     * @return
     */
    @Override
    public List<VideoQueryResult> searchVideos(String keyword, Integer pageSize, Integer pageIndex) {
        List<VideoQueryResult> videos;

        // 查询条件
        pageSize = pageSize == null ? 50 : pageSize;// 默认显示数量
        pageIndex = pageIndex == null ? Const.DEFAULT_PAGE_INDEX : (pageIndex - 1);

        Sort sort = new Sort(Sort.Direction.DESC, "likeAmount", "createdAt");
        Pageable page = new PageRequest(pageIndex, pageSize, sort);

        // 开始查询
        videos = uploadedVideoDao.findByNameContainingForMore(keyword, page).getContent();
        return videos;
    }

    /**
     * 搜索更多艺术团
     *
     * @param keyword  搜索的关键词
     * @param pageSize 搜索结果的数量
     * @return
     */
    @Override
    public List<ArtTeamSearchResult> searchTeams(String keyword, Integer pageSize, Integer pageIndex) {
        List<ArtTeamSearchResult> teams;

        // 查询条件
        pageSize = pageSize == null ? 50 : pageSize;// 默认显示数量
        pageIndex = pageIndex == null ? Const.DEFAULT_PAGE_INDEX : (pageIndex - 1);

        Sort sort = new Sort(Sort.Direction.DESC, "fansAmount", "createdAt");
        Pageable page = new PageRequest(pageIndex, pageSize, sort);

        // 执行查询
        teams = artTeamDao.findByNameContainingForMore(keyword, page).getContent();
        return teams;
    }


}
