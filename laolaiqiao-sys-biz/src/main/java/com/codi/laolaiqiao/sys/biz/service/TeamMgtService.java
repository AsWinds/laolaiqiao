package com.codi.laolaiqiao.sys.biz.service;

import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.result.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 团队管理Service
 * Created by song-jj on 2017/3/14.
 */
public interface TeamMgtService {

    /**
     * 分页查询团队
     * @param pageable
     * @param teamName
     * @return
     */
    Page<SearchResult> search(Pageable pageable, String teamName);

    Page<ArtTeamSearchResult> getTeamList(Pageable pageable, String teamName);

    void save(Long id, String teamName, String location, Long leaderId, String detail , String imageUrl);

    void delete(Long teamId);

    ArtTeamSearchResult detail(Long teamId);
}
