package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.result.SearchResult;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

public interface ArtTeamDao extends BaseRepository<ArtTeam, Long> {

	String JPQL_PREFIX = "select "
			+ "new com.codi.laolaiqiao.api.result.ArtTeamSearchResult"
				+ "(a.id, a.name, a.detail, a.imgUrl, u.name, u.address, a.likeAmount, a.fansAmount, a.leaderId) "
			+ "from ArtTeam a, User u "
			+ "where a.leaderId = u.id and u.isDisabled = false ";

	ArtTeam findByName(String string);

    /**
     * 搜索艺术团
     *
     * @param name 关键字
     * @return 艺术团列表
     */
    @Query("select "
    		+ "new com.codi.laolaiqiao.api.result.SearchResult(a.id, a.name, u.name) "
    		+ "from ArtTeam a, User u "
    		+ "where a.leaderId = u.id and u.isDisabled = false and lower( a.name ) like CONCAT('%', ?1,'%')")
    Page<SearchResult> findByNameContaining(String name, Pageable page);

    /**
     * 搜索艺术团更多信息
     *
     * @param name
     * @param page
     * @return
     */
    @Query(JPQL_PREFIX + " and a.name like CONCAT('%', ?1,'%')")
    Page<ArtTeamSearchResult> findByNameContainingForMore(String name, Pageable page);

    @Modifying
    @Query(value = "update "
    		+ "t_art_team set version = version + 1, fans_amount = fans_amount + ?2, last_update_time = ?3 "
    		+ "where id = ?1", nativeQuery = true)
    int updateFansAmount(Long teamId, Integer amount, Date currentTimestamp);

    @Query(JPQL_PREFIX)
    Page<ArtTeamSearchResult> findAllWithLeaderName(Pageable pageable);

    @Query(JPQL_PREFIX + "and a.id = ?1")
    ArtTeamSearchResult findOneById(Long id);


    String FIND_TEAM_LIST_JOIN_SQL = " left join t_user c on a.id = c.team_id and c.is_leader = true " +
        "left join (select u.team_id, count(u.id) as memberCount from t_user u where u.is_disabled = false group by u.team_id) b " +
        "on a.id = b.team_id";
    String FIND_TEAM_LIST_SQL = "SELECT a.id, a.name, a.created_at, a.location, c.name as userName, ifnull(b.memberCount,0) as memberCount FROM t_art_team a ";

    String FIND_TEAM_LIST_SQL_WHERE_CLAUSE = " where lower( a.name ) like CONCAT('%', ?3,'%') ";

    String FIND_TEAM_LIST_SQL_ORDER_BY = " order by a.created_at desc limit ?1,?2 ";
        /**
         * 查询团队列表（含团队人数）
         * @param recordIndex
         * @param size
         * @return
         */
    @Query(value = FIND_TEAM_LIST_SQL + FIND_TEAM_LIST_JOIN_SQL + FIND_TEAM_LIST_SQL_ORDER_BY, nativeQuery = true)
    List<Object[]> findAllTeamList(int recordIndex, int size);

    /**
     * 统计团队人数（含团队人数）
     * @return
     */
    @Query(value = "select count(a.id) from t_art_team a " + FIND_TEAM_LIST_JOIN_SQL, nativeQuery = true)
    int countAllTeam();

    /**
     * 根据团队名称查询团队（含团队人数）
     * @param recordIndex
     * @param size
     * @param teamName
     * @return
     */
    @Query(value = FIND_TEAM_LIST_SQL + FIND_TEAM_LIST_JOIN_SQL + FIND_TEAM_LIST_SQL_WHERE_CLAUSE + FIND_TEAM_LIST_SQL_ORDER_BY, nativeQuery = true)
    List<Object[]> findTeamByName(int recordIndex, int size, String teamName);

    /**
     * 根据团队名称统计团队（含团队人数）
     * @param teamName
     * @return
     */
    @Query(value = "select count(a.id) from t_art_team a " + FIND_TEAM_LIST_JOIN_SQL + " where lower( a.name ) like CONCAT('%', ?1,'%') ", nativeQuery = true)
    int countTeamByName(String teamName);

    /**
     * 锁表
     * @param id
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select new com.codi.laolaiqiao.api.domain.ArtTeam(a.id, a.leaderId) from ArtTeam a where a.id = ?1")
    ArtTeam findOneForUpdate(Long id);

    /**
     * 更新团队
     * @param teamName
     * @param location
     * @param leaderId
     * @param detail
     * @param imageUrl
     * @param now
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update t_art_team set version = version + 1, name = ?1, location = ?2, leader_id = ?3, detail = ?4, img_url = ?5, last_update_time = ?6 " +
        " where id = ?7", nativeQuery = true)
    int update(String teamName, String location, Long leaderId, String detail , String imageUrl, Date now, Long id);

    /***
     * 根据团长ID，查询团队
     * @return
     */
    ArtTeam findByLeaderId(Long leadId);
}
