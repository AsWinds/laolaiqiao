package com.codi.laolaiqiao.sys.biz.service;

import com.codi.laolaiqiao.api.result.ActivityResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 后台活动管理Service
 * Created by song-jj on 2017/3/16.
 */
public interface ActivityService {

    /**
     * 保存活动
     * @param id
     * @param imageUrl
     * @param name
     * @param startDate
     * @param location
     * @param detail
     */
    void save(Long id, String imageUrl, String name, Date startDate, Date endDate, String location, String detail, String contact, Long userId);

    /**
     * 活动列表
     * @param page 分页
     * @return
     */
    Page<ActivityResult> search(Pageable page);

    /**
     * 删除活动
     * @param id 活动ID
     */
    void delete(Long id);

    /**
     * 活动详情
     * @param id
     * @return
     */
    ActivityResult detail(Long id);
}
