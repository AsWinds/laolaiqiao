package com.codi.laolaiqiao.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.codi.laolaiqiao.common.entity.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * 验证码表
 *
 * */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SmsCode extends Domain{

	/**
	 *
	 * 验证码过期时间
	 *
	 * */
	@Column(updatable = false, nullable = false)
    private Date expireDate;

    /**
     *
     * 手机号码
     *
     * */
    @Column(nullable = false, updatable = false)
    private String phone;

    /**
     *
     * 验证码
     *
     * */
    @Column(updatable = false, nullable = false)
    private String smsCode;
}
