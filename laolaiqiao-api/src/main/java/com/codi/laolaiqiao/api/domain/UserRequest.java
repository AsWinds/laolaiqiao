package com.codi.laolaiqiao.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.codi.laolaiqiao.common.entity.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户请求
 * Created by song-jj on 2017/2/24.
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends Domain {

    /**
     * 用户名
     */
	@Column(nullable = false, updatable = false)
    private Long userId;

    /**
     * 请求时间戳, 当前以5s为单位
     */
	@Column(nullable = false)
    private Date requestTime;

}
