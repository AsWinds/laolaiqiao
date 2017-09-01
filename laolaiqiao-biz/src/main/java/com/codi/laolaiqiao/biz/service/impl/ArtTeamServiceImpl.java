package com.codi.laolaiqiao.biz.service.impl;

import com.codi.base.util.Assert;
import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.domain.UserJoinTeam;
import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.service.ArtTeamService;
import com.codi.laolaiqiao.biz.dao.ArtTeamDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.biz.dao.UserJoinTeamDao;
import com.codi.laolaiqiao.common.Const;
import com.codi.laolaiqiao.common.ErrorConst;
import com.codi.laolaiqiao.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 团队service实现类
 */
@Service
public class ArtTeamServiceImpl implements ArtTeamService {

	@Autowired
	private ArtTeamDao artDao;

	@Autowired
    private UserJoinTeamDao userJoinTeamDao;

	@Autowired
    private UserDao userDao;

	@Override
	public Page<ArtTeamSearchResult> getAll(Pageable pageable) {
		return artDao.findAllWithLeaderName(pageable);
	}

	@Override
    public ArtTeamSearchResult get(Long teamId, Long userId) {
		Assert.notNull(teamId);
	    ArtTeamSearchResult result = artDao.findOneById(teamId);

	    // 游客模式
	    if (userId == null) {
	        return result;
        }
	    // 查询当前用户跟团队的关系
        // 查询条件
        Sort sort = new Sort(Sort.Direction.DESC,  "createdAt");
        Pageable page = new PageRequest(0, 1, sort);

        List<UserJoinTeam> joinTeams = userJoinTeamDao.findByTeamIdAndUserId(teamId, userId, page);
        if (CollectionUtils.isEmpty(joinTeams)) {
            return result;
        }
        UserJoinTeam joinTeam = joinTeams.get(0);
        if (joinTeam != null) {
            result.setJoinStatus(joinTeam.getStatus());// 团队申请的状态
        }

	    return result;
    }

    /**
     * 用户申请加入团队
     *
     * @param teamId
     * @param userId
     */
    @Override
    public void join(Long teamId, Long userId) {
        // 团队ID或者用户ID为空的场合，报错：非法请求
        Assert.notNull(teamId);
        Assert.notNull(userId);

        // 查询团队是否存在
        isTeamExist(teamId);

        // 查询该用户用户是否存在，同时判断是否已加入团队
        User user = userDao.findOne(userId);

        // 用户不存在，返回
        if (user == null) {
            throw new BaseException(ErrorConst.ERROR_USER_NOT_FOUND);
        }

        // 已有团队，返回
        if (user.getTeamId() != null) {
            throw new BaseException(ErrorConst.ERROR_ONLY_ONE_TEAM_PERMIT);
        }

        // 查询该用户是否已经对该团队发送过申请（状态是auditing的）
        UserJoinTeam userJoinTeam = userJoinTeamDao.findByTeamIdAndUserIdAndStatus(teamId, userId, Const.USER_JOIN_TEAM_STATUS_AUDITING);
        // 已经申请过的，返回
        if (userJoinTeam != null) {
            throw new BaseException(ErrorConst.ERROR_HAS_APPLYED);
        }

        // TODO 删除以往的对该团队的请求记录

        // 以上以外的场合，将请求数据插入到数据库
        userJoinTeam = new UserJoinTeam(userId, teamId, Const.USER_JOIN_TEAM_STATUS_AUDITING);
        userJoinTeamDao.save(userJoinTeam);
    }

    /**
     * 判断团队是否存在；不存在的场合，报错
     * @param teamId
     */
    private void isTeamExist(Long teamId) {
        ArtTeam team = artDao.findOne(teamId);
        if (team == null) {
            throw new BaseException(ErrorConst.ERROR_TEAM_NOT_FOUND);
        }
    }

    /**
     * 审核用户加入团队的申请
     *
     * @param teamId      团队ID
     * @param userId      用户ID
     * @param auditUserId 审核的用户ID
     * @param status      审核结果：‘auditing’:审核中；rejected’:被拒绝；‘approved’：审核通过
     */
    @Override
    public void auditUserJoinTeamRequest(Long teamId, Long userId, Long auditUserId, String status) {
        // 团队ID或者用户ID或者审核用户ID为空的场合，报错
        Assert.notNull(teamId);
        Assert.notNull(userId);

        // 状态只能为指定的几种，以外的场合报错：非法请求
        if (!Arrays.asList(Const.USER_JOIN_TEAM_STATUSES).contains(status)) {
            throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
        }

        // 查询团队是否存在
        isTeamExist(teamId);

        // 锁表：团队申请表
        UserJoinTeam userJoinTeam = userJoinTeamDao.findForUpdate(teamId, userId);
        // 未找到，说明可能被其他团长同意了
        if (userJoinTeam == null) {
            throw new BaseException(ErrorConst.ERROR_HAS_BEEN_ACCEPTED);
        }

        // 更新申请状态
        Date now = new Date();
        userJoinTeamDao.updateJoinStatus(now, teamId, userId, status, auditUserId);

        // 如果被拒绝，则不用更新用户所属团队
        if (Const.USER_JOIN_TEAM_STATUS_REJECTED.equals(status)) {
            return;
        }

        // 锁表：用户表
        User lockedUser = userDao.findOneForUpdate(userId);
        // 如果用户不存在；报错
        if (lockedUser == null) {
            throw new BaseException(ErrorConst.ERROR_USER_NOT_FOUND);
        }

        // 该用户已经加入其他团队
        if (lockedUser.getTeamId() != null && !lockedUser.getTeamId().equals(teamId)) {
            throw new BaseException(ErrorConst.ERROR_HAS_BEEN_ACCEPTED);
        }

        // 更新用户所属的团队
        userDao.updateTeamId(now, teamId, userId);

        // 如果被接受；则删除相关的对其他团队的请求
        userJoinTeamDao.deleteByUserId(userId, teamId);

    }

    /**
     * 用户退出团队/团长移除用户
     *
     * @param teamId
     * @param userId
     */
    @Override
    public void quit(Long teamId, Long userId) {
        // 团队ID或者用户ID不能为空
        Assert.notNull(teamId);
        Assert.notNull(userId);

        // 查询团队是否存在
        isTeamExist(teamId);

        // 锁表：用户表
        User lockedUser = userDao.findOneForUpdate(userId);
        // 如果用户不存在；报错
        if (lockedUser == null) {
            throw new BaseException(ErrorConst.ERROR_USER_NOT_FOUND);
        }

        // 如果用户没有加入任何团队，报错：您尚未加入团队
        if (lockedUser.getTeamId() == null) {
            throw new BaseException(ErrorConst.ERROR_NOT_JOIN_ANY_TEAM);
        }

        // 更新用户所属的团队，将team_id 设成Null
        userDao.updateTeamId(new Date(), teamId, userId);

        // 删除所有相关申请请求
        userJoinTeamDao.deleteByUserId(userId);
    }


}
