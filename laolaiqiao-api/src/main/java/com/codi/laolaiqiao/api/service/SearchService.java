package com.codi.laolaiqiao.api.service;

import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.result.SearchResult;
import com.codi.laolaiqiao.api.result.VideoQueryResult;

import java.util.List;

/**
 * Created by song-jj on 2017/2/9.
 */
public interface SearchService {

    /**
     * 搜索视频/艺术团
     *
     * @param keyword  搜索的关键词
     * @param type     类型（0：视频；1：艺术团）
     * @param pageSize 搜索结果的数量
     * @return 视频或者艺术团列表
     */
    List<SearchResult> search(String keyword, String type, Integer pageSize, Integer pageIndex);

    /**
     * 搜索更多视频
     *
     * @param keyword  搜索的关键词
     * @param pageSize 搜索结果的数量
     * @return
     */
    List<VideoQueryResult> searchVideos(String keyword, Integer pageSize, Integer pageIndex);

    /**
     * 搜索更多艺术团
     * @param keyword 搜索的关键词
     * @param pageSize 搜索结果的数量
     * @return
     */
    List<ArtTeamSearchResult> searchTeams(String keyword, Integer pageSize, Integer pageIndex);
}
