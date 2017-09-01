package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;


public interface UserDao extends BaseRepository<User, Long> {

    String SQL_PAGE = "select u.id, u.created_at, u.phone, u.address, u.name, " +
        "case when u.is_disabled = 0 then '正常' " +
        "     when u.is_disabled = 1 then '禁用' " +
        "end as isDisabled,  " +
        "case when u.is_leader = 0 then '用户' " +
        "     when u.is_leader = 1 then '团长' " +
        "end as userCategory,  " +
        "t.name as teamName  from t_user u " +
        "left join t_art_team t on t.id = u.team_id";

    String SQL_COUNT = "select count(u.id) from t_user u left join t_art_team t on t.id = u.team_id ";

    String SQL_ORDER_BY = " order by u.created_at desc limit ?1, ?2";

    String SQL_FIND_USER = "select u.id, u.created_at, u.phone, u.address, u.name, " +
        "case when u.is_disabled = 0 then '正常' " +
        "     when u.is_disabled = 1 then '禁用' " +
        "end as isDisabled,  " +
        "case when u.is_leader = 0 then '用户' " +
        "     when u.is_leader = 1 then '团长' " +
        "end as userCategory,  " +
        "t.name as teamName, t.id as teamId from t_user u " +
        "left join t_art_team t on t.id = u.team_id";

	User findByPhone(String phone);

    /**
     * 分页统计所有的用户件数
     * @return
     */
    @Query(value = SQL_COUNT, nativeQuery = true)
	int countAllUsers();

    /**
     * 分页查询所有的
     * @param recordIndex
     * @param pageSize
     * @return
     */
	@Query(value = SQL_PAGE +  SQL_ORDER_BY, nativeQuery = true)
    List<Object[]> findAllWithPage(int recordIndex, int pageSize);

    /**
     * 分页统计所有的用户件数
     * @return
     */
    @Query(value = SQL_COUNT + " where u.name like concat('%', ?1,'%')", nativeQuery = true)
    int countUserByName(String name);

    /**
     * 分页查询所有的
     * @param recordIndex
     * @param pageSize
     * @return
     */
    @Query(value = SQL_PAGE + " where u.name like concat('%', ?3,'%')" + SQL_ORDER_BY, nativeQuery = true)
    List<Object[]> findByNameWithPage(int recordIndex, int pageSize, String name);

	@Query("SELECT u.id FROM User u WHERE  u.isDisabled = false and u.phone = ?1")
	Long findIdByPhone(String phone);

    /**
     * 锁表
     * @param id
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select new com.codi.laolaiqiao.api.domain.User(a.id, a.fansAmount, a.followAmount, a.teamId, a.name) from User a " +
        "where  a.isDisabled = false and a.id = ?1")
    User findOneForUpdate(Long id);

    @Modifying
    @Query(value = "update t_user set version = version + 1, fans_amount = fans_amount + ?2, last_update_time = ?3 " +
        "where is_disabled = false and id = ?1", nativeQuery = true)
    int updateFansAmount(Long userId, Integer amount, Date currentTimestamp);

    @Modifying
    @Query(value = "update t_user set version = version + 1, follow_amount = follow_amount + ?2, last_update_time = ?3 " +
        "where is_disabled = false and id = ?1", nativeQuery = true)
    int updateFollowAmount(Long userId, Integer amount, Date currentTimestamp);

    @Modifying
    @Query(value = "update t_user set version = version + 1, phone = ?1, name = ?2, address = ?3, team_id = ?4, last_update_time = ?5 " +
        " where id = ?6", nativeQuery = true)
    int update(String phone, String name, String address, Long teamId, Date currentTimestamp, Long id);

    /**
     * 更新用户状态
     * @param status
     * @param currentTimestamp
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update t_user set version = version + 1, is_disabled = ?1, last_update_time = ?2 " +
        " where id = ?3", nativeQuery = true)
    int updateStatus(Integer status, Date currentTimestamp, Long id);

    /**
     * 我的关注
     * @param id
     * @param page
     * @return
     */
    @Query("select new com.codi.laolaiqiao.api.domain.User(u.id, u.name, u.userImage) from Fans f, User u where u.id = f.userId and f.fansId = ?1 ")
    Page<User> findMyFollow(Long id, Pageable page);

    /**
     * 我的粉丝
     * @param id
     * @param page
     * @return
     */
    @Query("select new com.codi.laolaiqiao.api.domain.User(u.id, u.name, u.userImage) from Fans f, User u where u.id = f.fansId and f.userId = ?1")
    Page<User> findMyFans(Long id, Pageable page);

    @Query(value = SQL_FIND_USER + " where u.id = ?1 ", nativeQuery = true)
    List<Object[]> findById(Long id);

    /**
     * 变更团长
     * @param isLeader
     * @param now
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update t_user set version = version + 1, is_leader = ?1, last_update_time = ?2, team_id = ?4 " +
        " where id = ?3", nativeQuery = true)
    int updateLeader(boolean isLeader, Date now, Long id, Long teamId);

    /**
     * 更改用户所属团队
     * @param now
     * @param teamId
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update t_user set version = version + 1, last_update_time = ?1, team_id = ?2 " +
        " where id = ?3", nativeQuery = true)
    int updateTeamId(Date now, Long teamId, Long id);
}
