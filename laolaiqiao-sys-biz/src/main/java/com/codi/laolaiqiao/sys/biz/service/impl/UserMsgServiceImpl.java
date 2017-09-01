package com.codi.laolaiqiao.sys.biz.service.impl;

import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.common.ErrorConst;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.codi.laolaiqiao.common.util.ObjectUtil;
import com.codi.laolaiqiao.sys.biz.consts.SysErrorConst;
import com.codi.laolaiqiao.sys.biz.result.UserMsgSearchResult;
import com.codi.laolaiqiao.sys.biz.service.UserMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台用户管理Service
 * Created by song-jj on 2017/3/14.
 */
@Service
@Transactional
public class UserMsgServiceImpl implements UserMsgService {

    @Autowired
    private UserDao userDao;

    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 查询用户
     *
     * @param userName
     * @param page
     * @return
     */
    @Override
    public Page<UserMsgSearchResult> search(String userName, Pageable page) {
        Page<UserMsgSearchResult> result;
        List<Object[]> users;
        int countUsers;

        // 先count
        if (userName == null) {
            countUsers = userDao.countAllUsers();
        } else {
            countUsers = userDao.countUserByName(userName);
        }

        int pageSize = page.getPageSize() == 0 ? DEFAULT_PAGE_SIZE : page.getPageSize();
        int recordIndex = pageSize * page.getPageNumber();
        // 无数据
        if (countUsers == 0 || recordIndex > countUsers ) {
            return new PageImpl<>(new ArrayList<UserMsgSearchResult>());
        }

        // 用户名未填
        if (userName == null) {
            users = userDao.findAllWithPage(recordIndex, pageSize);
        } else {
            users = userDao.findByNameWithPage(recordIndex, page.getPageSize(), userName);
        }

        // 查询用户
        List<UserMsgSearchResult> userList = ObjectUtil.newObjs(UserMsgSearchResult.class, users);
        result = new PageImpl<>(userList, page, countUsers);

        return result;
    }

    /**
     * 保存
     * @param id
     * @param phone
     * @param name
     * @param address
     * @param idNo
     * @param teamId
     */
    public void save(Long id, String phone, String name, String address, String idNo, Long teamId) {
        User user = new User();
        // 手机号码不能为空
        if (StringUtils.isEmpty(phone)) {
            throw new BaseException(SysErrorConst.NOT_EMPTY, "手机号码");
        }

        // 用户名不能为空
        if (StringUtils.isEmpty(name)) {
            throw new BaseException(SysErrorConst.NOT_EMPTY, "用户名");
        }
        // 创建
        if(id == null) {
            // 设值
            setUser(phone, name, address, teamId, user);
            user.setId(id); // 主键
            user.setFansAmount(0);// 点赞量
            user.setFollowAmount(0);// 关注量
            user.setIsLeader(false);// 是否团长
            user.setIsDisabled(false);// 是否禁用
            user.setRole(User.ROLE_REG_USER);// 普通用户
            user.setUserImage(QiNiuManager.defaultUserPhoto); // 用户默认头像
            userDao.save(user);
            return;
        }
        // 更新
        // 锁表
        userDao.findOneForUpdate(id);

        // 更新
        userDao.update(phone, name, address, teamId, new Date(), id);

    }

    /**
     * 设值
     * @param phone
     * @param name
     * @param address
     * @param teamId
     * @param user
     */
    private void setUser(String phone, String name, String address, Long teamId, User user) {
        user.setTeamId(teamId); // 小组ID
        user.setAddress(address);
        user.setPhone(phone);
        user.setName(name);
    }

    /**
     * 更新用户状态
     * @param id
     * @param status
     */
    public void updateStatus(Long id, Integer status) {
        if (status != 0 && status != 1) {
            throw new BaseException(ErrorConst.ERROR_INVALID_REQUEST);
        }
        userDao.updateStatus(status, new Date(), id);
    }

    /**
     * 根据ID查询用户详情
     *
     * @param id
     * @return
     */
    @Override
    public UserMsgSearchResult queryUserById(Long id) {
        List<UserMsgSearchResult> users = ObjectUtil.newObjs(UserMsgSearchResult.class, userDao.findById(id));
        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        }
        throw new BaseException(SysErrorConst.USER_NOT_FOUNT);
    }
}
