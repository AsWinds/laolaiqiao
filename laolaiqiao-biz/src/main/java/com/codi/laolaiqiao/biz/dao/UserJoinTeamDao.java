package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.UserJoinTeam;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;


/**
 * 用户加入团队的申请DAO
 * Created by song-jj on 2017/3/31.
 */
public interface UserJoinTeamDao extends BaseRepository<UserJoinTeam, Long> {

    /**
     * 根据团队ID和用户ID查询用户加入团队的申请
     * @param teamId
     * @param userId
     * @param page
     * @return
     */
    @Query("select new com.codi.laolaiqiao.api.domain.UserJoinTeam(a.userId, a.teamId, a.status) from UserJoinTeam a " +
        "where a.teamId = ?1 and a.userId = ?2")
    List<UserJoinTeam> findByTeamIdAndUserId(Long teamId, Long userId, Pageable page);

    /**
     * 取得指定状态下的用户申请
     * @param teamId
     * @param page
     * @return
     */
    @Query("select new com.codi.laolaiqiao.api.domain.User(u.id, u.name, u.userImage, u.address) from UserJoinTeam a, User u where a.teamId = ?1 ")
    List<UserJoinTeam> findByTeamId(Long teamId, Pageable page);

    /**
     * 查询指定用户，对某团队的申请状态
     * @param teamId
     * @param userId
     * @param status
     * @return
     */
    UserJoinTeam findByTeamIdAndUserIdAndStatus(Long teamId, Long userId, String status);

    /**
     * 根据团队ID和用户ID，锁表
     * @param teamId 团队ID
     * @param userId 用户ID
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select new com.codi.laolaiqiao.api.domain.UserJoinTeam(a.userId, a.teamId, a.status) from UserJoinTeam a " +
        " where a.teamId = ?1 and a.userId = ?2 ")
    UserJoinTeam findForUpdate(Long teamId, Long userId);

    /**
     * 更新团队申请的状态
     * @param now
     * @param teamId
     * @param userId
     * @param status
     * @return
     */
    @Modifying
    @Query(value = "update t_user_user_join set version = version + 1, last_update_time = ?1, status = ?4, audit_user_id = ?5 " +
        " where team_id = ?2 and user_id = ?3 ", nativeQuery = true)
    int updateJoinStatus(Date now, Long teamId, Long userId, String status, Long auditUserId);

    /**
     * 删除指定用户对其他团队的加入请求
     * @param userId
     * @param teamId
     * @return
     */
    @Modifying
    @Query(value = "delete from t_user_user_join where user_id = ?1 and team_id != ?2 ", nativeQuery = true)
    int deleteByUserId(Long userId, Long teamId);

    /**
     * 删除所有的请求记录
     * @param userId
     * @return
     */
    @Modifying
    @Query(value = "delete from t_user_user_join where user_id = ?1 ", nativeQuery = true)
    int deleteByUserId(Long userId);
}
