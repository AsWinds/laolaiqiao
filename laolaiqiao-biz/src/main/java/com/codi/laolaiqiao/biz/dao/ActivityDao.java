package com.codi.laolaiqiao.biz.dao;

import com.codi.laolaiqiao.api.domain.Activity;
import com.codi.laolaiqiao.common.jdbc.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActivityDao extends BaseRepository<Activity, Long> {

    String SQL_COLUMN = "a.id, a.name, a.detail, a.img_url, a.location, a.contact, a.start_date, a.end_date, a.publisher_id, u.USER_NAME";
    String SQL_COUNT_COLUMN = " count(a.id)";
    String SQL_TABLE = " FROM t_activity AS a, SYS_USER AS u WHERE a.publisher_id = u.USER_ID";
    String SQL_ORDER_BY_CLAUSE = " order by a.start_date limit ?1, ?2";
	String SQL_PREFIX = "SELECT " + SQL_COLUMN + SQL_TABLE + SQL_ORDER_BY_CLAUSE;

    /**
     * 分页查询
     * @return
     */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	@Query(value = SQL_PREFIX, nativeQuery = true)
	List<Object[]> findAllActivityResult(int recordIndex, int size);

    String SQL_COUNT = "SELECT " + SQL_COUNT_COLUMN + SQL_TABLE ;

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Query(value = SQL_COUNT, nativeQuery = true)
    int countAllActivityResult();
}
