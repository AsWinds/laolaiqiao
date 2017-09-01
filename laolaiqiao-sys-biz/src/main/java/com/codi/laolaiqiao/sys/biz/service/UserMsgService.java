package com.codi.laolaiqiao.sys.biz.service;

import com.codi.laolaiqiao.sys.biz.result.UserMsgSearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 后台用户管理
 * Created by song-jj on 2017/3/14.
 */
public interface UserMsgService {

    /**
     * 查询用户
     * @param userName
     * @param page
     * @return
     */
    Page<UserMsgSearchResult> search(String userName, Pageable page);

    /**
     * 保存
     * @param id
     * @param phone
     * @param name
     * @param address
     * @param idNo
     * @param teamId
     */
    void save(Long id, String phone, String name, String address, String idNo, Long teamId);

    /**
     * 更改用户状态
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据ID查询用户详情
     * @param id
     * @return
     */
    UserMsgSearchResult queryUserById(Long id);

}
