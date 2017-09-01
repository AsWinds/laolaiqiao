package com.codi.laolaiqiao.sys.biz.schedule;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.codi.base.util.DateUtil;
import com.codi.laolaiqiao.biz.dao.RemberMeTokenDao;
import com.codi.laolaiqiao.common.util.TimeUtil;

@Component
public class CleanRemberMeTokenTask extends SimpleTransactionTask<Integer> {

	@Autowired
	private RemberMeTokenDao rmtDao;

	@PostConstruct
	public void scheduled() {
		scheduledExecutorService.scheduleAtFixedRate(this, 1, 24, TimeUnit.HOURS);
	}

	@Override
	public Integer doTransaction(TransactionStatus status) {
		Date lastDay = new Date(System.currentTimeMillis() - TimeUtil.DAY_AS_MILLS);
		int row = rmtDao.deleteByExpireAtBefore(lastDay);
		log.info("删除{}之前的{}条过期RemberMeToken", DateUtil.format(lastDay), row);
		return row;
	}

}
