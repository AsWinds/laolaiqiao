package com.codi.laolaiqiao.sys.biz.result;

import com.codi.base.util.DateUtils;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * 后台查询用户
 * Created by song-jj on 2017/3/14.
 */
@Data
public class UserMsgSearchResult {

    public UserMsgSearchResult(BigInteger id, Timestamp createdAt, String phone, String address, String userName, String userStatus, String userCategory, String teamName) {
        this.id = id.longValue();
        this.userName = userName;
        this.phone = phone;
        this.createdAt = DateUtils.dateToStr(createdAt, "yyyy-MM-dd HH:mm");
        this.userCategory = userCategory;
        this.userStatus = userStatus;
        this.address = address;
        this.teamName = teamName;
    }

    public UserMsgSearchResult(BigInteger id, Timestamp createdAt, String phone, String address, String userName, String userStatus, String userCategory, String teamName, BigInteger teamId) {
        this.id = id.longValue();
        this.userName = userName;
        this.phone = phone;
        this.createdAt = DateUtils.dateToStr(createdAt, "yyyy-MM-dd HH:mm");
        this.userCategory = userCategory;
        this.userStatus = userStatus;
        this.address = address;
        this.teamName = teamName;
        if (teamId != null) {
            this.teamId = teamId.longValue();
        }
    }

    /**
     * ID
     */
    private Long id;

    private String userName;

    private String phone;

    private String createdAt;

    private String userCategory;

    private String userStatus;

    private String address;

    private String teamName;

    private Long teamId;

}
