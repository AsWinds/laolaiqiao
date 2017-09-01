package com.codi.laolaiqiao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.converter.ActivityConverter;
import com.codi.laolaiqiao.biz.dao.ActivityDao;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.CollectionResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController {
	
	@Autowired
	private ActivityDao activityDao;
	
	private final ActivityConverter converter = new ActivityConverter();
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public BaseResult getAllActivitys(Pageable pageable){
		if (pageable == null) {
	        pageable = new PageRequest(0, 10, Direction.DESC, "startDate");
        }
		return new CollectionResult<>(activityDao.findAll(pageable).map(converter));
	}
	
	@RequestMapping("/detail/{id}")
	public BaseResult activityDetail(@PathVariable("id") Long id){
		return new SingleDataResult<>(converter.convert(activityDao.findOne(id)));
	}

}
