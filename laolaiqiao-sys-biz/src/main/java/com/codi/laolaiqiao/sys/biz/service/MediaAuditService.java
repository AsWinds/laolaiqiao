package com.codi.laolaiqiao.sys.biz.service;

import com.codi.laolaiqiao.api.result.AuditVideoToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MediaAuditService {

	String getPrivateVideoAccessUrl(Long videoTokenId);

	void auditVideo(Long videoTokenId, boolean booleanValue, Long sysUserId);

	Page<AuditVideoToken> getNeedAuditVideoTokens(Pageable pageable);

}
