package com.codi.laolaiqiao.common.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.qiniu.QiNiuManager;
import com.qiniu.util.Auth;

/**
 * 
 * validate the coming request is come from QiNiu ?
 * 
 * */
public class QiNiuCallbackValidationFilter extends OncePerRequestFilter {
	
	private final Auth auth;
	private final HashMap<String, String> mapper = new HashMap<>();
	
	public QiNiuCallbackValidationFilter() {
		String accessKey = QiNiuManager.accessKey;
		String secretKey = QiNiuManager.secretKey;
		auth = Auth.create(accessKey, secretKey);
		mapper.put("uploadVideoComplete", QiNiuManager.uploadVideoCallbackUrl);
		mapper.put("transcodeAndCreateThumbnailCallback", QiNiuManager.transcodeAndCreateThumbnailCallbackUrl);
		mapper.put("uploadImgComplete", QiNiuManager.uploadImgCallbackUrl);
	}

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		String cbAuthHeader = request.getHeader("Authorization");
		if (StringUtils.isBlank(cbAuthHeader)) {
			throw new BaseException("9001");
		}
		ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper)request;
		byte[] body = getBody(wrapper);
		if (body == null || body.length == 0) {
			throw new BaseException("9001");
		}
		String configCBUrl = getCBUrl(request.getRequestURI());
		if (configCBUrl == null) {
			throw new BaseException("9001");
		}
		boolean validCallback = auth.isValidCallback(cbAuthHeader, configCBUrl, body, "application/json");
		if (validCallback) {
			filterChain.doFilter(request, response);
		}else{
			throw new BaseException("9001");
		}
    }
	
	private byte[] getBody(ContentCachingRequestWrapper request) throws IOException{
		if (request.getContentType() != null && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE))
		{
			InputStream inputStream = request.getInputStream();
			if (inputStream != null)
			{
				if (request.getContentLength() == -1) {
					return IOUtils.toByteArray(inputStream);
				}else {
					return IOUtils.toByteArray(inputStream, request.getContentLength());
				}
			}
		}
		return null;
	}
	
	private String getCBUrl(String cbUrl){
		for (Map.Entry<String, String> entry : mapper.entrySet()) {
			if (cbUrl.endsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}


}
