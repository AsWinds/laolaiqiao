package com.laolaiqiao.web.servlet.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.laolaiqiao.web.security.auth.UserDetailsImpl;

/**
 * 用户相关工具类
 * Created by song-jj on 2017/2/27.
 */
public class UserUtil {

    /**
     * 取得的用户ID
     * @return
     */
    public static Long getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
	        return null;
        }
        UserDetailsImpl impl = (UserDetailsImpl) auth.getPrincipal();
        return impl.getUserId();
    }
}
