package com.codi.laolaiqiao.biz.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.codi.laolaiqiao.api.domain.VideoUploadToken;
import com.codi.laolaiqiao.common.jdbc.UploadDoDao;

public interface VideoUploadTokenDao extends UploadDoDao<VideoUploadToken, Long> {

	@Query(value = "SELECT "
			+ "vut.id, vut.title, u.phone, vut.owner_team_id, vut.status, vut.last_update_time "
			+ "FROM t_video_upload_token vut, t_user u "
			+ "WHERE vut.user_id = u.id AND vut.upload_complete = TRUE AND vut.status = 1 AND vut.expire_time < ?1 limit ?2, ?3", nativeQuery = true)
	List<Object[]> findByExpireTimeBefore(Long date, int recordIndex, int pageSize);

    @Query(value = "SELECT "
        + "count(vut.id) "
        + "FROM t_video_upload_token vut, t_user u "
        + "WHERE vut.user_id = u.id AND vut.upload_complete = TRUE AND vut.status = 1 AND vut.expire_time < ?1", nativeQuery = true)
    int countByExpireTimeBefore(Long date);

	@Query("select v.id "
			+ "from VideoUploadToken v "
			+ "where v.uploadComplete = ?1 and v.expireTime < ?2 and v.status = ?3")
	Page<Long> findIdByUploadCompleteAndExpireTimeBeforeAndStatus(boolean uploadComplete, Long date, int status, Pageable pageable);

}
