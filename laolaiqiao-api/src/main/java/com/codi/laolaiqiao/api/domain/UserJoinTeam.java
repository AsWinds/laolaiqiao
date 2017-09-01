package com.codi.laolaiqiao.api.domain;

import com.codi.laolaiqiao.common.entity.Domain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by song-jj on 2017/3/31.
 */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserJoinTeam extends Domain {
    /**
     * 申请加入团队的用户ID
     */
    private Long userId;

    /**
     * 被申请加入的团队ID
     */
    private Long teamId;

    /**
     * 申请状态
     * ‘auditing’:审核中；
     * ‘rejected’:被拒绝；
     * ‘approved’：审核通过
     */
    private String status;

    /**
     * 负责审核的用户ID;一般为团长
     */
    private Long auditUserId;

    public UserJoinTeam(Long userId, Long teamId, String status) {
        this.userId = userId;
        this.teamId = teamId;
        this.status = status;
    }
}
