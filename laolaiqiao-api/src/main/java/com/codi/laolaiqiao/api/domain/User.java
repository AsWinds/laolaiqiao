package com.codi.laolaiqiao.api.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.codi.laolaiqiao.common.entity.Domain;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 *
 * 用户表
 *
 * */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends Domain {

    public static final int ROLE_REG_USER = 0;
    public static final int ROLE_APPROVE = 3;
    public static final int ROLE_OFFICIAL = 7;

    public static final List<Integer> ROLES = Collections.unmodifiableList(Arrays.asList(ROLE_REG_USER, ROLE_APPROVE, ROLE_OFFICIAL));

    /**
     *
     * 创建一个用户
     *
     * */
    public User(String phone, String name, int role, Boolean isDisabled){
    	this.fansAmount = 0;
    	this.followAmount = 0;
    	this.isDisabled = isDisabled;
    	this.isLeader = false;
    	this.name = name;
    	this.phone = phone;
    	this.role = role;
    	this.teamId = null;
    	this.userImage = QiNiuManager.defaultUserPhoto;
    }

    public User(Long id, Integer fansAmount, Integer followAmount){
        setId(id);
        this.fansAmount = fansAmount;
        this.followAmount = followAmount;
    }

    public User(Long id, String name, String userImage){
        setId(id);
        this.name = name;
        this.userImage = QiNiuManager.getImgFileUrl(userImage);
    }

    public User(Long id, String name, String userImage, String address){
        setId(id);
        this.name = name;
        this.userImage = QiNiuManager.getImgFileUrl(userImage);
        this.address = address;
    }

    public User(Long id, Integer fansAmount, Integer followAmount, Long teamId, String name){
        setId(id);
        this.fansAmount = fansAmount;
        this.followAmount = followAmount;
        this.teamId = teamId;
        this.name = name;
    }

    /**
     * 姓名
     */
    @Column(nullable = false)
    private String name;

    /**
     * 电话号码
     */
    @Column(updatable = false, unique = true, nullable = false)
    private String phone;

    /**
     * 地址, 如果province, city, district有设值, 其值为这三个字段相连
     * province, city, district这三个字段, 要么都有值, 要么都没值
     */
    @Column(nullable = true, updatable = true)
    private String address;
    
    /**
     * 省, 直辖市
     * */
    @Column(nullable = true, updatable = true)
    private String province;
    
    /**
     * 城市
     * */
    @Column(nullable = true, updatable = true)
    private String city;
    
    /**
     * 区
     * */
    @Column(nullable = true, updatable = true)
    private String district;
    
    /**
     * 街道
     * */
    @Column(nullable = true, updatable = true)
    private String street;

    /**
     * 0, 一般注册用户
     * 3, 认证用户
     * 7, 系统用户 这个用户属于老来俏团队
     *
     */
    @Column(nullable = false)
    private Integer role;

    /**
     * 用户所属的团队ID
     */
    private Long teamId;

    /**
     * 用户头像
     */
    @Column(nullable = false)
    private String userImage;

    /**
     * 用户是否被禁用
     */
    @Column(nullable = false)
    private Boolean isDisabled;

    /**
     *  是否团队队长
     */
    @Column(nullable = false)
    private Boolean isLeader;

    /**
     * 粉丝数
     */
    @Column(nullable = false)
    private Integer fansAmount;

    /**
     * 关注数
     */
    @Column(nullable = false)
    private Integer followAmount;
    
    @PrePersist
    @PreUpdate
    public void initAddress(){
    	if (province == null || city == null || district == null) {
			this.province = null;
			this.city = null;
			this.district = null;
		}else {
			this.address = province + city + district;
		}
    }

}
