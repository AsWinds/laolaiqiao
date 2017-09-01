package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.UploadedVideo;
import com.codi.laolaiqiao.api.result.SearchResult;
import com.codi.laolaiqiao.api.result.VideoQueryResult;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

/**
 * 视频DAO
 * Created by song-jj on 2017/2/9.
 */
public interface UploadedVideoDao extends BaseRepository<UploadedVideo, Long> {

    String SQL_BY_PAGE = "select "
    		+ "new com.codi.laolaiqiao.api.result.VideoQueryResult(a.id, a.name, t.name, a.likeAmount, a.visitAmount, a.thumbnailUrl) "
    		+ "from UploadedVideo a, ArtTeam t "
    		+ "where a.ownerTeamId = t.id ";

    String SQL_BY_PAGE_ALL = "select "
    		+ "new com.codi.laolaiqiao.api.result.VideoQueryResult(a.id, a.name, u.name, a.likeAmount, a.visitAmount, a.thumbnailUrl) "
    		+ "from UploadedVideo a, User u "
    		+ "where a.uploadUserId = u.id ";


    /**
     * 搜索视频
     *
     * @param name 关键字
     * @return 视频列表
     */
    @Query("select "
    		+ "new com.codi.laolaiqiao.api.result.SearchResult(a.id, a.name, u.name) "
    		+ "from UploadedVideo a, User u "
    		+ "where a.uploadUserId = u.id and a.name like CONCAT('%', ?1,'%')")
    Page<SearchResult> findByNameContaining(String name, Pageable page);


    /**
     * 搜索视频更多信息
     *
     * @param name
     * @param page
     * @return
     */
    @Query("select "
    		+ "new com.codi.laolaiqiao.api.result.VideoQueryResult(a.id, a.name, u.name, t.name, a.likeAmount, a.visitAmount, a.thumbnailUrl) "
    		+ "from UploadedVideo a, User u, ArtTeam t "
    		+ "where a.uploadUserId = u.id and t.id = u.teamId and u.isDisabled = false "
    			+ "and a.name like CONCAT('%', ?1,'%') and a.category = 1 ")
    Page<VideoQueryResult> findByNameContainingForMore(String name, Pageable page);


    /**
     * 分页查询视频
     * @param page 分页条件
     * @return
     */
    @Query(SQL_BY_PAGE_ALL)
    Page<VideoQueryResult> findByPage(Pageable page);


    String SQL_VIDEO_SEARCH = "select "
    		+ "v.id, v.name, u.name as userName, t.name as teamName, v.like_amount, v.visit_amount, v.thumbnail_url from t_uploaded_video v "
    		+ "left join t_art_team t on t.id = v.owner_team_id "
    		+ "inner join t_user u on u.id = v.upload_user_id "
    		+ "where u.is_disabled = false and and v.upload_user_id = ?1 ";

    String SQL_VIDEO_COUNT = "select "
    		+ "count(v.id) from t_uploaded_video v "
    		+ "left join t_art_team t on t.id = v.owner_team_id "
    		+ "inner join t_user u on u.id = v.upload_user_id "
    		+ "where u.is_disabled = false and v.upload_user_id = ?1 ";

    /**
     * 查询用户下的视频
     * @param userId 分页条件
     * @return
     */
    @Query(value = "select "
    		+ "v.id, v.name, u.name as userName, t.name as teamName, v.like_amount, v.visit_amount, v.thumbnail_url "
    		+ "from t_uploaded_video v "
    		+ "left join t_art_team t on t.id = v.owner_team_id "
    		+ "inner join t_user u on u.id = v.upload_user_id "
    		+ "where u.is_disabled = false and  v.upload_user_id = ?1 order by v.created_at desc limit ?2, ?3 ",
    		nativeQuery = true)
    List<Object[]> findAuditedByUserId(Long userId, int recordIndex, int pageSize);

    /**
     * 根据用户ID统计已审核视频的数量
     * @param userId
     * @return
     */
    @Query(value = SQL_VIDEO_COUNT, nativeQuery = true)
    int countAuditedByUserId(Long userId);

    /**
     * 分页查询所有视频
     * @param userId
     * @param recordIndex
     * @param pageSize
     * @return
     */
    @Query(value = "select "
    		+ "v.id, a.title, u.name as userName, t.name as teamName, "
    		+ "ifnull(v.like_amount, 0), ifnull(v.visit_amount, 0), v.thumbnail_url, a.status "
    		+ "from t_video_upload_token a "
    		+ "left join t_art_team t on t.id = a.owner_team_id "
    		+ "inner join t_user u on u.id = a.user_id "
    		+ "left join t_uploaded_video v on v.uvt_id = a.id "
    		+ "where u.is_disabled = false and a.user_id = ?1 and a.status != -1 and a.upload_complete = true "
    		+ "order by a.created_at desc limit ?2, ?3 ", nativeQuery = true)
    List<Object[]> findAllByUserId(Long userId, int recordIndex, int pageSize);

    /**
     * 统计所有上传完毕的视频
     * @param userId
     * @return
     */
    @Query(value = "select "
    		+ "count(a.id) from  t_video_upload_token a "
    		+ "left join t_art_team t on t.id = a.owner_team_id "
    		+ "inner join t_user u on u.id = a.user_id "
    		+ "left join t_uploaded_video v on v.uvt_id = a.id "
    		+ "where u.is_disabled = false and a.user_id = ?1 and a.upload_complete = true ",
    		nativeQuery = true)
    int countAllByUserId(Long userId);

    /**
     * 锁表
     * @param id
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select "
    		+ "new com.codi.laolaiqiao.api.domain.UploadedVideo(a.id, a.likeAmount, a.visitAmount, a.version, "
    			+ "a.name, a.url, a.thumbnailUrl, a.uploadUserId, a.ownerTeamId, a.category) "
    		+ "from UploadedVideo a "
    		+ "where a.id = ?1")
    UploadedVideo findOneForUpdate(Long id);

    /**
     * 更新点赞量
     * @param videoId
     * @return
     */
    @Modifying
    @Query(value = "update t_uploaded_video "
    		+ "set version = version + 1, like_amount = like_amount + ?2, last_update_time = ?3 "
    		+ "where id = ?1", nativeQuery = true)
    int updateLikeAmount(Long videoId, Integer amount, Date currentTimestamp);

    /**
     * 更新浏览量
     * @param videoId
     * @return
     */
    @Modifying
    @Query(value = "update t_uploaded_video "
    		+ "set version = version + 1, visit_amount = visit_amount + ?2, last_update_time = ?3 "
    		+ "where id = ?1", nativeQuery = true)
    int updateVisitAmount(Long videoId, Integer amount, Date currentTimestamp);

	UploadedVideo findByUvtId(Long vutId);

    /**
     * 根据团队ID，取得团队视频
     * @param teamId 团队ID
     * @param page 分页信息
     * @return 视频列表
     */
    @Query("select "
        + "new com.codi.laolaiqiao.api.result.VideoQueryResult(a.id, a.name, u.name, t.name, a.likeAmount, a.visitAmount, a.thumbnailUrl) "
        + "from UploadedVideo a, User u, ArtTeam t "
        + "where a.uploadUserId = u.id and a.ownerTeamId = t.id and u.isDisabled = false "
        + " and a.ownerTeamId = ?1 and a.category = 1 ")
    Page<VideoQueryResult> findTeamVideo(Long teamId, Pageable page);
}
