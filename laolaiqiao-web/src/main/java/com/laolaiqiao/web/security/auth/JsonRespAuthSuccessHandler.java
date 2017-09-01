package com.laolaiqiao.web.security.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.SingleDataResult;
import com.laolaiqiao.web.servlet.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonRespAuthSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private UserDao userDao;

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
		response.setStatus(HttpStatus.OK.value());
	    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
	    BaseResult result = new SingleDataResult<>(userDao.findOne(UserUtil.getUserId()));
	    JSON.writeJSONString(response.getWriter(), result, SerializerFeature.QuoteFieldNames, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
	    response.flushBuffer();
    }

}
