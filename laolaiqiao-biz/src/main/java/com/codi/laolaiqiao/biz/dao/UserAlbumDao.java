package com.codi.laolaiqiao.biz.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.codi.laolaiqiao.api.domain.UserAlbum;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;

public interface UserAlbumDao extends BaseRepository<UserAlbum, Long> {

	UserAlbum findByUserIdAndName(Long userId, String album);
	
	long countByUserId(Long userId);
	
	@Query(value = "SELECT ua.id, ua.name, ui.bucket, ui.stored_key "
			+ "FROM t_user_album AS ua LEFT JOIN "
				+ "(SELECT album_id, bucket, stored_key "
				+ "FROM t_user_img "
				+ "WHERE upload_complete = TRUE AND user_id = ?1) as ui "
			+ "ON ua.id = ui.album_id "
			+ "WHERE ua.user_id = ?1 GROUP BY ua.id "
			+ "LIMIT ?2, ?3 ", 
			nativeQuery = true)
	List<Object[]> findAlbumAndLatestImg(Long userId, int startIndex, int pageSize);

}
