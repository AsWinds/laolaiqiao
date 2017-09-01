package com.codi.laolaiqiao.common;

/**
 * 模块名
 *
 * @author shi.pengyan
 * @date 2016-12-28 15:31
 */
public interface ErrorConst {

    /***
     * 系统错误
     */
    String ERROR_TYPE_SYSTEM = "SYS";

    /***
     * 应用错误
     */
    String ERROR_TYPE_APPLICATION = "APC";

    /***
     * 业务错误
     */
    String ERROR_TYPE_BUSINESS = "BUS";

    // 一般错误消息FIELD占位符
    String PLACEHOLDER_FIELD = "FIELD";

    // Error Code
    String ERROR_INVALID_REQUEST= "0002";

    /**
     * 用户未登录
     */
    String ERROR_USER_NOT_LOGIN= "1001";

    // 重复提交评论
    String ERROR_DUPLICATE_SUBMIT = "1003";

    // 未找到视频
    String ERROR_VIDEO_NOT_FOUND = "1004";

    // 非法评论
    String ERROR_COMMMENT_INVALID = "1005";

    // 无该排序字段
    String ERROR_ORDER_BY_NOT_FOUND = "1006";

    // 用户不存在
    String ERROR_USER_NOT_FOUND = "1007";

    // 自己不能关注自己
    String ERROR_FOLLOW_SELF = "1008";
    // 团队不存在
    String ERROR_TEAM_NOT_FOUND = "1009";

    // 您只能加入一个团队
    String ERROR_ONLY_ONE_TEAM_PERMIT = "1010";

    // 您已经申请过了
    String ERROR_HAS_APPLYED = "1011";

    // 用户已经被其他团队接受了
    String ERROR_HAS_BEEN_ACCEPTED = "1012";

    // 尚未加入任何团队
    String ERROR_NOT_JOIN_ANY_TEAM = "1013";
}
