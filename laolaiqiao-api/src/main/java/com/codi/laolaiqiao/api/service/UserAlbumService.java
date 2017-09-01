package com.codi.laolaiqiao.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codi.laolaiqiao.api.result.UserAlbumResult;
import com.codi.laolaiqiao.api.result.UserImgResult;

public interface UserAlbumService {
	
	Page<UserAlbumResult> getUserAlbums(Long userId, Pageable pageable);

	Page<UserImgResult> getAlbumImgs(Long id, Pageable pageable);

}
