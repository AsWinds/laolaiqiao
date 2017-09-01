package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.domain.Fans;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户/团队 粉丝（关注） 表
 * Created by song-jj on 2017/3/6.
 */
public interface FansDao extends BaseRepository<Fans, Long> {

    @Query("select count(id) from Fans where fansId = ?1 and teamId = ?2 and category = 1")
    int countByFansIdAndTeamId(Long fansId, Long teamId);

    @Query("select count(id) from Fans where fansId = ?1 and userId = ?2 and category = 0")
    int countByFansIdAndUserId(Long fansId, Long userId);

    @Modifying
    @Query(value = "delete from t_fans where category = 1 and team_id = ?1 and fans_id = ?2 ", nativeQuery = true)
    int deleteTeamFans(Long teamId, Long fansId);

    @Modifying
    @Query(value = "delete from t_fans where category = 0 and user_id = ?1 and fans_id = ?2 ", nativeQuery = true)
    int deleteUserFans(Long userId, Long fansId);

    /**
     * 我关注的团队
     * @param id
     * @param page
     * @return
     */
    @Query("select new com.codi.laolaiqiao.api.domain.ArtTeam(t.id, t.name, t.imgUrl) from Fans f, ArtTeam t where t.id = f.teamId and f.fansId = ?1 ")
    Page<ArtTeam> findMyFollowedTeam(Long id, Pageable page);
}
