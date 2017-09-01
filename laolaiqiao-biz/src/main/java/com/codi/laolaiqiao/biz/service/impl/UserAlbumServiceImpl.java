package com.codi.laolaiqiao.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codi.laolaiqiao.api.domain.UserImg;
import com.codi.laolaiqiao.api.result.UserAlbumResult;
import com.codi.laolaiqiao.api.result.UserImgResult;
import com.codi.laolaiqiao.api.service.UserAlbumService;
import com.codi.laolaiqiao.biz.dao.UserAlbumDao;
import com.codi.laolaiqiao.biz.dao.UserImgDao;
import com.codi.laolaiqiao.common.util.ObjectUtil;

@Service
public class UserAlbumServiceImpl implements UserAlbumService {
	
	@Autowired
	private UserAlbumDao uaDao; 
	
	@Autowired
	private UserImgDao uiDao;

	@Override
	public Page<UserAlbumResult> getUserAlbums(Long userId, Pageable pageable) {
		long total = uaDao.countByUserId(userId);
		int start = pageable.getOffset(), size = pageable.getPageSize();
		if (total == 0 || start > total) {
			return new PageImpl<>(new ArrayList<>(0), pageable, total);
		}
		List<Object[]> data = uaDao.findAlbumAndLatestImg(userId, start, size);
		return new PageImpl<>(ObjectUtil.newObjs(UserAlbumResult.class, data), pageable, total);
	}

	@Override
	public Page<UserImgResult> getAlbumImgs(Long id, Pageable pageable) {
		Page<UserImg> page = uiDao.findByAlbumIdAndUploadComplete(id, true,pageable);
		return page.map(new Converter<UserImg, UserImgResult>() {

			@Override
			public UserImgResult convert(UserImg source) {
				return new UserImgResult(source);
			}
		});
	}

}
