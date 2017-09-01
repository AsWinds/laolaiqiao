package com.laolaiqiao.web.security.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codi.laolaiqiao.api.domain.SmsCode;
import com.codi.laolaiqiao.api.domain.User;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final String username;
    private final String smsCode;
    private final Long userId;
    private final Date expireAt;
    private final Boolean isDisabled;

    /**
     *
     * 验证码登录时username是phone
     *
     * */
    public UserDetailsImpl(SmsCode code, User user) {
    	this.username = code.getPhone();
    	this.smsCode = code.getSmsCode();
    	this.userId = user.getId();
    	this.expireAt = code.getExpireDate();
    	this.isDisabled = user.getIsDisabled();
    }

    /**
     *
     * token登录时username也是phone, 但没有smsCode信息
     *
     * */
    public UserDetailsImpl(User user) {
    	this.username = user.getPhone();
    	this.smsCode = null;
    	this.userId = user.getId();
    	this.expireAt = null;
    	this.isDisabled = user.getIsDisabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	    // TODO Auto-generated method stub
	    return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

	@Override
    public String getPassword() {
	    return smsCode;
    }

	@Override
    public String getUsername() {
	    return username;
    }

	@Override
    public boolean isAccountNonExpired() {
		return true;
    }

	@Override
    public boolean isAccountNonLocked() {
	    return true;
    }

	@Override
    public boolean isCredentialsNonExpired() {
		if (expireAt == null) {
	        return true;
        }
	    return expireAt.compareTo(new Date()) > 0;
    }

	@Override
    public boolean isEnabled() {
	    return BooleanUtils.isFalse(isDisabled);
    }

	public Long getUserId(){
		return userId;
	}

}
