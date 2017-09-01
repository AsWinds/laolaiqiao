package com.codi.laolaiqiao.api.domain;

import com.codi.laolaiqiao.common.entity.Domain;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * 艺术团表
 *
 * */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ArtTeam extends Domain {

    /**
     * 艺术团名称
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * 位置
     */
    private String location;

    /**
     * 详情
     */
    private String detail;

    /**
     *
     * 默认团队头像
     *
     * */
    @Column(nullable = false)
    private String imgUrl;

    /**
     * 团长id, 映射User表
     */
    private Long leaderId;

    /**
     * 关注量
     */
    @Column(nullable = false)
    private Integer fansAmount;

    /**
     * 点赞数量
     */
    @Column(nullable = false)
    private Integer likeAmount;

    public ArtTeam(Long id, Long leaderId) {
        setId(id);
        this.leaderId = leaderId;
    }

    public ArtTeam(Long id, String name, String imgUrl) {
        setId(id);
        this.name = name;
        this.imgUrl = QiNiuManager.getImgFileUrl(imgUrl);
    }
}
