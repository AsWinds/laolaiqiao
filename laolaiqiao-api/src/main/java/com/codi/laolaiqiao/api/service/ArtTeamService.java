package com.codi.laolaiqiao.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codi.laolaiqiao.api.result.ArtTeamSearchResult;

public interface ArtTeamService {

	Page<ArtTeamSearchResult> getAll(Pageable pageable);

    /**
     * 取得团队详情
     * @param id
     * @param userId
     * @return
     */
	ArtTeamSearchResult get(Long id, Long userId);

    /**
     * 用户申请加入团队
     * @param teamId
     * @param userId
     */
	void join(Long teamId, Long userId);

    /**
     * 审核用户加入团队的申请
     * @param teamId 团队ID
     * @param userId 用户ID
     * @param auditUserId 审核的用户ID
     * @param status 审核结果：‘auditing’:审核中；rejected’:被拒绝；‘approved’：审核通过
     */
	void auditUserJoinTeamRequest(Long teamId, Long userId, Long auditUserId, String status);

    /**
     * 用户退出团队
     * @param teamId
     * @param userId
     */
	void quit(Long teamId, Long userId);


}
