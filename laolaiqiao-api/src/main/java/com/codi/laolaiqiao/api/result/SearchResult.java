package com.codi.laolaiqiao.api.result;

import lombok.Data;

/**
 * 视频搜索结果
 * Created by song-jj on 2017/2/9.
 */
@Data
public class SearchResult {

    /**
     * 资源ID
     */
    private Long id;

    /**
     * 视频或者艺术团名称
     */
    private String name;

    /**
     * 视频上传者
     */
    private String userName;

    /**
     * 构造方法
     *
     * @param id       资源ID
     * @param name     视频或者艺术团名称
     * @param userName 视频上传者
     */
    public SearchResult(Long id, String name, String userName) {
        this.id = id;
        this.name = name;
        this.userName = userName;
    }

}
