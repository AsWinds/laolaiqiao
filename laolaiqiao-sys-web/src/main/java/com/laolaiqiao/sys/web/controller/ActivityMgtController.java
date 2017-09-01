package com.laolaiqiao.sys.web.controller;

import com.codi.laolaiqiao.api.result.ActivityResult;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.sys.biz.service.ActivityService;
import com.laolaiqiao.sys.web.result.SysResult;
import com.laolaiqiao.sys.web.security.SysUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping(value = "/activity")
public class ActivityMgtController extends BaseController {

    @Autowired
    private ActivityService activityService;

    /**
     * 分页查询所有的活动
     * @param page
     * @return
     */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public BaseResult all(Pageable page){
		Page<ActivityResult> res = activityService.search(page);
		return new SysResult(res);
	}

    /**
     * 保存活动
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult save(Long id, String imageFileName, String name, Date startDate, Date endDate, String location, String detail, String contact){
        Long sysUserId = SysUserUtil.getLoginUser().getUserId();
        activityService.save(id, imageFileName, name, startDate, endDate, location, detail, contact, sysUserId);
        return new BaseResult(true);
    }

    /**
     * 活动详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public SysResult detail(@PathVariable("id") Long id){
        return new SysResult(activityService.detail(id));
    }

    /**
     * 删除活动
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public BaseResult delete(@PathVariable("id") Long id){
        activityService.delete(id);
        return new BaseResult(true);
    }

}
