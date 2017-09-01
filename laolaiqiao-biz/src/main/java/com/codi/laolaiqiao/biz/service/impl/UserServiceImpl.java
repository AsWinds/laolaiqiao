package com.codi.laolaiqiao.biz.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.domain.Fans;
import com.codi.laolaiqiao.api.domain.UploadedVideo;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.req.UserInfo;
import com.codi.laolaiqiao.api.result.UserResult;
import com.codi.laolaiqiao.api.service.UserService;
import com.codi.laolaiqiao.biz.dao.ArtTeamDao;
import com.codi.laolaiqiao.biz.dao.FansDao;
import com.codi.laolaiqiao.biz.dao.UploadedVideoDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.common.Const;
import com.codi.laolaiqiao.common.ErrorConst;
import com.codi.laolaiqiao.common.exception.BaseException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
    private UploadedVideoDao videoDao;

	@Autowired
    private FansDao fansDao;

	@Autowired
    private ArtTeamDao artTeamDao;

    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
	@Override
	public User findOne(long id) {
		return userDao.findOne(id);
	}

    /**
     * 关注
     * @param videoId
     * @param userId
     * @param amount
     */
    @Override
    public void follow(long videoId, long userId, Integer amount) {

        // 根据视频ID，查询该视频是团队的还是个人的
        UploadedVideo video = videoDao.findOne(videoId);
        if (video == null) {
            throw new BaseException(ErrorConst.ERROR_VIDEO_NOT_FOUND);
        }

        // 个人
        if (UploadedVideo.CATEGORY_PERSONAL == video.getCategory()) {
            followPerson(userId, amount, video.getUploadUserId());
            return;
        }
        // 团队
        followTeam(userId, amount, video.getOwnerTeamId());
    }

    private void updateFollowAmount(long userId, Integer amount, Date now) {
        // 悲观锁
        userDao.findOneForUpdate(userId);
        // 更新当前用户的关注数（这个关注数到底是团队+个人的）
        userDao.updateFollowAmount(userId, amount, now);
    }

    /**
     * 关注团队
     * @param userId
     * @param amount
     * @param followedTeamId
     */
    @Override
    public void followTeam(Long userId, Integer amount, Long followedTeamId) {
        Date now = new Date();
        int count = fansDao.countByFansIdAndUserId(userId, followedTeamId);

        // 取消关注
        if (amount < 0) {
            if (count != 1) {
                throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
            }
            fansDao.deleteTeamFans(followedTeamId, userId);
        } else {
            if (count > 0) {
                throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
            }

            // 关注
            fansDao.save(Fans.teamFans(userId, followedTeamId));
        }

        // 悲观锁
        User lockedUser = userDao.findOneForUpdate(userId);
        // 用户不存在
        if (lockedUser == null) {
            throw new BaseException(ErrorConst.ERROR_USER_NOT_FOUND);
        }

        // 锁表-团队
        ArtTeam team = artTeamDao.findOneForUpdate(followedTeamId);
        // 未找到团队
        if (team == null) {
            throw new BaseException(ErrorConst.ERROR_TEAM_NOT_FOUND);
        }

        // 更新被关注团队的粉丝数
        artTeamDao.updateFansAmount(followedTeamId, amount, now);

        // 更新当前用户的关注数
        updateFollowAmount(userId, amount, now);
    }

    /**
     * 我关注的团队
     *
     * @param userId
     * @param pageSize
     * @param pageIndex
     */
    @Override
    public Page<ArtTeam> myFollowedTeam(Long userId, Integer pageSize, Integer pageIndex) {
        // 参数
        pageSize = pageSize == null ? 15 : pageSize;// 默认显示数量
        pageIndex = pageIndex == null ? Const.DEFAULT_PAGE_INDEX : (pageIndex - 1);

        // 分页条件
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable page = new PageRequest(pageIndex, pageSize, sort);

        // 查询
        Page<ArtTeam> teams = fansDao.findMyFollowedTeam(userId, page);

        return teams;
    }

    /**
     * 关注个人
     * @param userId
     * @param amount 数量等于1：关注；数量等于-1：取消关注
     * @param followedUserId
     */
    @Override
    public void followPerson(Long userId, Integer amount, Long followedUserId) {
        // 自己关注自己
        if (followedUserId.equals(userId)) {
            throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
        }
        Date now = new Date();

        int count = fansDao.countByFansIdAndUserId(userId, followedUserId);

        // 取消关注
        if (amount < 0) {
            if (count != 1) {
                throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
            }
            fansDao.deleteUserFans(followedUserId, userId);
        } else {
            if (count > 0) {
                throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
            }
            fansDao.save(Fans.userFans(userId, followedUserId));
        }

        // 悲观锁
        User lockedUser = userDao.findOneForUpdate(followedUserId);
        // 用户不存在
        if (lockedUser == null) {
            throw new BaseException(ErrorConst.ERROR_USER_NOT_FOUND);
        }
        // 更新被关注用户的粉丝数
        userDao.updateFansAmount(followedUserId, amount, now);

        // 更新当前用户的关注数
        updateFollowAmount(userId, amount, now);
    }

    @Override
    public User createUserByPhone(String phone) {
		User user = userDao.findByPhone(phone);
		if (user != null) {
	        return user;
        }
	    user = new User();
	    user.setPhone(phone);
	    user = userDao.save(user);
	    logger.info("Create User with phone: {}, id: {}", phone, user.getId());
	    return user;
    }

    /**
     * 查询其他用户的详情信息
     *
     * @param currentUserId 当前用户ID
     * @param otherUserId 他人ID
     * @return
     */
    @Override
    public UserResult findUserDetail(Long currentUserId, Long otherUserId) {

        Long userId = otherUserId == null ? currentUserId : otherUserId;// 当前需要查询的用户ID
        // 查询用户信息
        User user = userDao.findOne(userId);
        Assert.notNull(user);

        UserResult result = new UserResult();
        BeanUtils.copyProperties(user, result);// 用户信息

        // 如果是他人页面，查询当前用户是否关注了该用户
        Boolean isFollow = false;
        if (otherUserId != null && currentUserId != null) {
            isFollow = fansDao.countByFansIdAndUserId(currentUserId, otherUserId) > 0;
        }
        result.setIsFollow(isFollow);

        return result;
    }

    /**
     * 查询我的粉丝/我的关注
     * @param pageSize
     * @param pageIndex
     * @param userId
     * @param type 0:关注；1：粉丝
     * @return
     */
    @Override
    public Page<User> queryFans(Integer pageSize, Integer pageIndex, Long userId, int type) {
        // 参数
        pageSize = pageSize == null ? 15 : pageSize;// 默认显示数量
        pageIndex = pageIndex == null ? Const.DEFAULT_PAGE_INDEX : (pageIndex - 1);

        // 分页条件
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable page = new PageRequest(pageIndex, pageSize, sort);

        Page<User> users;
        // 我的关注
        if (type == 0) {
            users = userDao.findMyFollow(userId, page);

            // 我的粉丝
        } else {
            users = userDao.findMyFans(userId, page);
        }
        return users;
    }

	@Override
	public UserResult updateUser(Long userId, UserInfo info) {
		User user = userDao.findOne(userId);
		BeanUtils.copyProperties(info, user);
		user = userDao.save(user);
		UserResult result = new UserResult();
		BeanUtils.copyProperties(user, result);
		return result;
	}
}
