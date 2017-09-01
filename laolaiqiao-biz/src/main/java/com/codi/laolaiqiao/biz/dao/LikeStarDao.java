package com.codi.laolaiqiao.biz.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codi.laolaiqiao.api.domain.LikeStar;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;

/**
 * 点赞和收藏DAO
 * Created by song-jj on 2017/2/22.
 */
public interface LikeStarDao extends BaseRepository<LikeStar, Long> {

    /**
     * 根据视频ID和用户ID查询点赞和收藏
     * @param videoId
     * @param userId
     * @return
     */
    @Query("select new com.codi.laolaiqiao.api.domain.LikeStar(a.type) from LikeStar a " +
        "where a.userId = ?2 and a.videoId = ?1")
    List<LikeStar> findByVideoIdAndUserId(Long videoId, Long userId);

    /**
     *
     * @param videoId
     * @param userId
     * @return
     */
    @Query("select count(a.type) from LikeStar a where a.userId = ?2 and a.videoId = ?1 and a.type = ?3")
    int count(Long videoId, Long userId, String type);

    /**
     * 取消点赞或者收藏
     * @param videoId
     * @param userId
     * @param type
     * @return
     */
    @Modifying
    @Query("delete from LikeStar a where a.videoId = ?1 and a.userId = ?2 and a.type = ?3")
    Integer deleteLikeOrStar(Long videoId, Long userId, String type);

    /**
     * 查询用户下的视频
     * @param userId 分页条件
     * @return
     */
    @Query(value = "select v.id, v.name, u.name as userName, t.name as teamName, v.like_amount, v.visit_amount, v.thumbnail_url from t_like_star s " +
        " inner join t_uploaded_video v on s.video_id = v.id  " +
        " left join t_art_team t on t.id = v.owner_team_id " +
        " inner join t_user u on u.id = v.upload_user_id " +
        " where u.is_disabled = false and s.user_id = ?1 and s.type = '1' order by v.created_at desc limit ?2, ?3",nativeQuery = true)
    List<Object[]> findByUserId(Long userId, int recordIndex, int pageSize);

    /**
     * 根据用户ID统计已审核视频的数量
     * @param userId
     * @return
     */
    @Query(value = "select count(v.id) from t_like_star s " +
        " inner join t_uploaded_video v on s.video_id = v.id  " +
        " left join t_art_team t on t.id = v.owner_team_id " +
        " inner join t_user u on u.id = v.upload_user_id " +
        " where u.is_disabled = false and s.user_id = ?1 and s.type = '1' ",nativeQuery = true)
    int countByUserId(Long userId);
}
