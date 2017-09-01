package com.codi.laolaiqiao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.service.ArtTeamService;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.CollectionResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;

@RestController
@RequestMapping("/artTeam/")
public class ArtTeamQueryController {

    @Autowired
    private ArtTeamService artService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public BaseResult artTeams(Pageable pageable){
    	if (pageable == null) {
	        pageable = new PageRequest(0, 10, Direction.ASC, "name");
        }
    	return new CollectionResult<>(artService.getAll(pageable));
    }





}
