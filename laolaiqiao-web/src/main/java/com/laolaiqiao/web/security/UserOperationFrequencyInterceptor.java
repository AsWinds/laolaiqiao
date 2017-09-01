package com.laolaiqiao.web.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.codi.laolaiqiao.api.domain.UserRequest;
import com.codi.laolaiqiao.biz.dao.UserRequestDao;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.laolaiqiao.web.servlet.util.UserUtil;

@Slf4j
public class UserOperationFrequencyInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserRequestDao userRequestDao;

	@Override
    public boolean preHandle(HttpServletRequest req,
            HttpServletResponse response, Object handler) throws Exception {
	    String method = req.getMethod();
	    Long userId = UserUtil.getUserId();
	    if (!"POST".equals(method) || userId == null) {
	    	return true;
        }
	    long now = System.currentTimeMillis();
	    long nowSeconds = getOperationSecond(now);
	    UserRequest userRequest = userRequestDao.findByUserId(userId);
	    if (userRequest == null) {
	    	log.debug("Create UserRequest for UserID: " + userId);
	    	userRequest = new UserRequest(userId, new Date(now));
	    	userRequest = userRequestDao.save(userRequest);
	    	return true;
        }
	    long lastSeconds = getOperationSecond(userRequest.getRequestTime().getTime());
	    if (lastSeconds == nowSeconds) {
	        throw new BaseException("1003");
        }
	    //更新当前操作时间
	    log.debug("Update UserRequest for User ID:" + userId);
	    userRequest.setRequestTime(new Date(now));
	    userRequestDao.save(userRequest);
	    return true;
    }
	
	private long getOperationSecond(long timeStamp){
		return timeStamp / 5000;
	}

	@Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
	    // TODO Auto-generated method stub
	    
    }


}
