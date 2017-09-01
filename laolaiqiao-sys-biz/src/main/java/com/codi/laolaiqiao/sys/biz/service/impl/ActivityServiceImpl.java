package com.codi.laolaiqiao.sys.biz.service.impl;


import com.codi.laolaiqiao.api.domain.Activity;
import com.codi.laolaiqiao.api.result.ActivityResult;
import com.codi.laolaiqiao.biz.dao.ActivityDao;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.util.ObjectUtil;
import com.codi.laolaiqiao.sys.biz.consts.SysErrorConst;
import com.codi.laolaiqiao.sys.biz.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台活动管理Service实现类
 * Created by song-jj on 2017/3/16.
 */
@Transactional
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    /**
     * 保存活动
     *
     * @param id
     * @param imageUrl
     * @param name
     * @param startDate
     * @param location
     * @param detail
     */
    @Override
    public void save(Long id, String imageUrl, String name, Date startDate, Date endDate, String location, String detail, String contact, Long userId) {

        Activity activity;

        // 检查输入合法性
        checkInput(name, startDate, endDate, detail, contact);


        // 插入
        if (id == null) {
            activity = new Activity(imageUrl, name, startDate, endDate, location, detail, contact, userId);
        } else {
            activity = activityDao.findOne(id);
            // 设值
            activity.update(imageUrl, name, startDate, endDate, location, detail, contact, userId);
        }
        // 保存
        activityDao.save(activity);

    }

    @Override
    public Page<ActivityResult> search(Pageable page) {
        // 先统计下
        int recordCount = activityDao.countAllActivityResult();

        int recordIndex=  page.getPageNumber() * page.getPageSize();
        // 如果没有更多的数据，返回
        if (recordCount == 0 || recordIndex > recordCount) {
            new PageImpl<>(new ArrayList<ActivityResult>());
        }

        // 查询活动列表
        List<ActivityResult> activities = ObjectUtil.newObjs(ActivityResult.class, activityDao.findAllActivityResult(page.getPageNumber(), page.getPageSize()));

        // 判断状态
        for (ActivityResult activity : activities) {
            // 如果当前时间大于开始时间，则活动结束；否则报名中
            if (new Date().compareTo(activity.getStartDate()) > 0) {
                activity.setStatus(ActivityResult.STATUS_FINISHED);
            }
        }

        // 返回数据
        return new PageImpl<>(activities, page, recordCount);
    }

    /**
     * 删除活动
     *
     * @param id 活动ID
     */
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new BaseException(SysErrorConst.INVALID_REQUEST);
        }
        activityDao.delete(id);
    }

    /**
     * 活动详情
     *
     * @param id
     * @return
     */
    @Override
    public ActivityResult detail(Long id) {
        // 查询活动详情
        Activity activity = activityDao.findOne(id);

        return new ActivityResult(activity);
    }


    /**
     * 检查输入参数的合法性
     * @param name
     * @param startDate
     * @param endDate
     * @param detail
     * @param contact
     */
    private void checkInput(String name, Date startDate, Date endDate, String detail, String contact) {
        // 活动名称不能为空
        if (StringUtils.isEmpty(name)) {
            throw new BaseException(SysErrorConst.NOT_EMPTY, "活动名称");
        }

        // 开始日期不能为空
        if (startDate == null) {
            throw new BaseException(SysErrorConst.NOT_EMPTY, "开始日期");
        }

        // 结束日期不能为空
        if (endDate == null) {
            throw new BaseException(SysErrorConst.NOT_EMPTY, "结束日期");
        }

        // 活动详情不能为空
        if (StringUtils.isEmpty(detail)) {
            throw new BaseException(SysErrorConst.NOT_EMPTY, "活动详情");
        }

        // 联系方式不能为空
        if (StringUtils.isEmpty(contact)) {
            throw new BaseException(SysErrorConst.NOT_EMPTY, "联系方式");
        }
    }


}
