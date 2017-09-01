package com.codi.laolaiqiao.api.result;

import javax.persistence.Column;

import com.codi.laolaiqiao.common.qiniu.QiNiuManager;

import lombok.Data;

/**
 * 用户查询类
 * Created by song-jj on 2017/3/6.
 */
@Data
public class UserResult {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 姓名
     */
    @Column(nullable = false)
    private String name;

    /**
     * 地址
     */
    private String address;
    
    /**
     * 省, 直辖市
     * */
    private String province;
    
    /**
     * 城市
     * */
    private String city;
    
    /**
     * 区
     * */
    private String district;
    
    /**
     * 街道
     * */
    private String street;

    /**
     * 用户头像
     */
    private String userImage;

    /**
     * 粉丝数
     */
    private Integer fansAmount;

    /**
     * 关注数
     */
    private Integer followAmount;

    /**
     * 是否关注该用户
     */
    private Boolean isFollow;

    public void setUserImage(String userImage) {
        this.userImage = QiNiuManager.getImgFileUrl(userImage);
    }

}
