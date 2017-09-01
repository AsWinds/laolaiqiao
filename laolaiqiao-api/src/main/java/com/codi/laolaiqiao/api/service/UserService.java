package com.codi.laolaiqiao.api.service;

import org.springframework.data.domain.Page;

import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.req.UserInfo;
import com.codi.laolaiqiao.api.result.UserResult;

public interface UserService {

	User findOne(long id);

	void follow(long videoId, long userId, Integer amount);

	/**
	 *
	 * 创建一个User, 如果phone对应的用户已存在, 直接返回原User, 如果不存在, 则创建一条新的记录,
	 * 然后返回新创建的User
	 *
	 * */
	User createUserByPhone(String phone);


    /**
     * 查询其他用户的详情信息
     *
     * @param currentUserId 当前用户ID
     * @param otherUserId 他人ID
     * @return
     */
    UserResult findUserDetail(Long currentUserId, Long otherUserId);

    /**
     * 关注个人
     * @param userId
     * @param amount
     * @param followedUserId
     */
    void followPerson(Long userId, Integer amount, Long followedUserId);

    /**
     * 关注团队
     * @param userId
     * @param amount
     * @param followedTeamId
     */
    void followTeam(Long userId, Integer amount, Long followedTeamId);

    /**
     * 我关注的团队
     * @param userId
     * @param pageSize
     * @param pageIndex
     */
    Page<ArtTeam> myFollowedTeam(Long userId, Integer pageSize, Integer pageIndex);

    /**
     * 查询我的粉丝/我的关注
     * @param pageSize
     * @param pageIndex
     * @param userId
     * @param type 0:关注；1：粉丝
     * @return
     */
    Page<User> queryFans(Integer pageSize, Integer pageIndex, Long userId, int type);

    /**
     * 更新指定userId的用户信息
     * */
	UserResult updateUser(Long userId, UserInfo info);

}
