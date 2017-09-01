package com.codi.laolaiqiao.biz.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codi.laolaiqiao.api.domain.UserImg;
import com.codi.laolaiqiao.common.jdbc.UploadDoDao;

public interface UserImgDao extends UploadDoDao<UserImg, Long>{

	Page<UserImg> findByAlbumIdAndUploadComplete(Long id, boolean b, Pageable pageable);

}
