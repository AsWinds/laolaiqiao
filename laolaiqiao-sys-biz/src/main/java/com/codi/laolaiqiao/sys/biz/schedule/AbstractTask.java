package com.codi.laolaiqiao.sys.biz.schedule;

import java.sql.Date;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * Task基类, 主要用于日志记录Task的运行时间, 捕获异常.
 * 
 * 每个任务都应在3s中之内结束掉, 如果要处理的数据太多,
 * 
 * 业务应设计为分批多次执行, 默认任务执行时间超过2s就会报警日志
 * 
 */
public abstract class AbstractTask implements Runnable {

	private static final int WARN_MILLS = 2000;

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	@Autowired
	protected TaskScheduler taskScheduler;

	@Autowired
	protected ScheduledExecutorService scheduledExecutorService;
	
	@Autowired
	protected TransactionTemplate transactionTemplate;

	@Scheduled(fixedRate = 3000)
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			doBiz();
		} catch (Exception e) {
			log.error("Job Error, startTime:" + new Date(start), e);
		} finally {
			long end = System.currentTimeMillis();
			long cost = end - start;
			if (cost > WARN_MILLS) {
				log.warn("Job Execute Time WARN, start:{}, end:{}, cost:{}", start, end, cost);
			}
		}
	}

	protected abstract void doBiz();

}
