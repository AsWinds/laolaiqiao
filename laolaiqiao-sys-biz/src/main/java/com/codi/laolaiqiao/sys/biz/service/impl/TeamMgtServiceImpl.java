package com.codi.laolaiqiao.sys.biz.service.impl;

import com.codi.laolaiqiao.api.domain.ArtTeam;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;
import com.codi.laolaiqiao.api.result.SearchResult;
import com.codi.laolaiqiao.biz.dao.ArtTeamDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.codi.laolaiqiao.common.util.ObjectUtil;
import com.codi.laolaiqiao.sys.biz.consts.SysErrorConst;
import com.codi.laolaiqiao.sys.biz.service.TeamMgtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 团队管理
 * Created by song-jj on 2017/3/14.
 */
@Service
@Transactional
public class TeamMgtServiceImpl implements TeamMgtService {

    @Autowired
    private ArtTeamDao teamDao;

    @Autowired
    private UserDao userDao;

    /**
     * 团队检索：用于其他模块的所属团队
     * @param pageable
     * @param teamName
     * @return
     */
    @Override
    public Page<SearchResult> search(Pageable pageable, String teamName) {
        if (StringUtils.isEmpty(teamName)) {
            return new PageImpl<>(new ArrayList<SearchResult>());
        }
        return teamDao.findByNameContaining(teamName, pageable);
    }

    /**
     * 取得团队列表
     * @param pageable
     * @param teamName
     * @return
     */
    @Override
    public Page<ArtTeamSearchResult> getTeamList(Pageable pageable, String teamName) {
        int recordIndex = pageable.getPageNumber() * pageable.getPageSize();
        // 团队名称为空
        if (StringUtils.isEmpty(teamName)) {
            return getAllTeam(recordIndex, pageable.getPageSize(), pageable);
        }

        // 根据团队名称模糊查询
        return getTeamByName(teamName, recordIndex, pageable.getPageSize(), pageable);
    }

    /**
     * 分页取得所有的团队列表
     * @param recordIndex
     * @param pageSize
     * @param pageable
     * @return
     */
    private Page<ArtTeamSearchResult> getAllTeam(int recordIndex, int pageSize, Pageable pageable) {
        int teamCount = teamDao.countAllTeam();
        // 无更多数据
        if (teamCount == 0 || teamCount < recordIndex) {
            return new PageImpl<>(new ArrayList<ArtTeamSearchResult>());
        }

        // 查询
        List<ArtTeamSearchResult> teams = ObjectUtil.newObjs(ArtTeamSearchResult.class, teamDao.findAllTeamList(recordIndex, pageSize));

        return new PageImpl<>(teams, pageable, teamCount);
    }

    /**
     * 根据团队名称曾分页取得团队列表
     * @param teamName
     * @param recordIndex
     * @param pageSize
     * @param pageable
     * @return
     */
    public Page<ArtTeamSearchResult> getTeamByName(String teamName, int recordIndex, int pageSize, Pageable pageable) {
        int teamCount = teamDao.countTeamByName(teamName);
        // 无更多数据
        if (teamCount == 0 || teamCount < recordIndex) {
            return new PageImpl<>(new ArrayList<ArtTeamSearchResult>());
        }

        // 查询
        List<ArtTeamSearchResult> teams = ObjectUtil.newObjs(ArtTeamSearchResult.class, teamDao.findTeamByName(recordIndex, pageSize, teamName));

        return new PageImpl<>(teams, pageable, teamCount);
    }

    /**
     * 保存
     * @param id
     * @param teamName
     * @param location
     * @param leaderId
     * @param detail
     * @param imageUrl
     */
    @Override
    public void save(Long id, String teamName, String location, Long leaderId, String detail , String imageUrl) {
        ArtTeam team;
        Date now = new Date();

        // 查询该用户是否已经是其他团队的团队
        ArtTeam existTeam = teamDao.findByLeaderId(leaderId);
        if (existTeam != null && !existTeam.getId().equals(id)) {
            throw new BaseException(SysErrorConst.TEAM_LEADER_EXIST);
        }

        // 更新用户is_leader字段，设为团长
        // 锁表
        userDao.findOneForUpdate(leaderId);

        // 更新
        userDao.updateLeader(true, now, leaderId, id);

        // 新建团队
        if (id == null) {
            team = new ArtTeam();
            setArtTeam(team, teamName, location, leaderId, detail , imageUrl);
            team.setFansAmount(0);
            team.setLikeAmount(0);
            teamDao.save(team);
            return;
        }

        // 更新团队
        // 查询锁表
        team = teamDao.findOneForUpdate(id);

        // 未查到团队，报错
        if (team == null) {
            throw new BaseException(SysErrorConst.TEAM_NOT_FOUNT);
        }

        // 如果团长变更了，将原来团长的is_leader 字段设为false
        if (!team.getLeaderId().equals(leaderId)) {
            // 锁表
            userDao.findOneForUpdate(leaderId);

            // 更新
            userDao.updateLeader(true, now, leaderId, id);
        }

        // 更新团队
        teamDao.update(teamName, location, leaderId, detail, imageUrl, new Date(), id);
    }

    private void setArtTeam(ArtTeam team, String teamName, String location, Long leaderId, String detail , String imageUrl) {
        team.setName(teamName);// 团队名称
        team.setLocation(location);// 团队地址
        team.setLeaderId(leaderId); // 团长ID
        team.setDetail(detail);// 团队简介
        team.setImgUrl(imageUrl == null ? QiNiuManager.defaultTeamPhoto : imageUrl);// 团队图片
    }

    /**
     * 删除团队
     * @param teamId
     */
    @Override
    public void delete(Long teamId) {
        teamDao.delete(teamId);
    }

    /**
     * 根据团队ID查询团队详情
     * @param teamId
     * @return
     */
    @Override
    public ArtTeamSearchResult detail(Long teamId) {
        ArtTeamSearchResult teamDetail = new ArtTeamSearchResult();

        // 团队信息
        ArtTeam team = teamDao.findOne(teamId);
        // 团队不存在
        if (team == null) {
            throw new BaseException(SysErrorConst.TEAM_NOT_FOUNT);
        }

        BeanUtils.copyProperties(team, teamDetail);
        teamDetail.setAddress(team.getLocation());// 地址
        String imgUrl = team.getImgUrl();
        teamDetail.setImageFileName(imgUrl);// 图片文件名
        teamDetail.setImgUrl(QiNiuManager.getImgFileUrl(imgUrl));// 图片全路径

        // 团长名
        User teamLeader = userDao.findOne(team.getLeaderId());
        if (teamLeader != null) {
            teamDetail.setUserName(teamLeader.getName());
        }

        return teamDetail;
    }
}
