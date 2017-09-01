package com.codi.laolaiqiao.common;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2016-12-28 15:31
 */
public interface Const {

    /**
     * 分页查询默认一页显示的数量
     */
    int SEARCH_RESULT_SIZE = 15;

    /**
     * 分页查询默认第几页
     */
    int DEFAULT_PAGE_INDEX = 0;

    String HEADER_APP_DEVICE = "App-Device";

    /**
     * 审核中
     */
    String USER_JOIN_TEAM_STATUS_AUDITING = "auditing";

    /**
     * 被拒绝
     */
    String USER_JOIN_TEAM_STATUS_REJECTED = "rejected";

    /**
     * 已同意
     */
    String USER_JOIN_TEAM_STATUS_APPROVED = "approved";

    /**
     * 用户状态LIST
     */
    String[] USER_JOIN_TEAM_STATUSES = {USER_JOIN_TEAM_STATUS_AUDITING, USER_JOIN_TEAM_STATUS_REJECTED, USER_JOIN_TEAM_STATUS_APPROVED};
}
