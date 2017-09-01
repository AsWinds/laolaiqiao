package com.codi.backgroundservice.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.codi.backgroundservice.service.BaseService;

public class ServiceThreadPoolExecutor extends ThreadPoolExecutor{

	private String executorCode;

	public String getExecutorCode() {
		return executorCode;
	}

	public void setExecutorCode(String executorCode) {
		this.executorCode = executorCode;
	}

	public ServiceThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) { 
		// 输出此ThreadPool的状态
		Logger logger = Logger.getLogger(this.getClass());
		StringBuilder sb = new StringBuilder();
		sb.append("线程池");
		sb.append("[");
		sb.append(this.getExecutorCode());
		sb.append("]\r\n当前运行情况\r\nPoolSize:");
		sb.append(this.getPoolSize());
		sb.append("\r\nMaximumPoolSize:");
		sb.append(this.getMaximumPoolSize());
		sb.append("\r\nLargestPoolSize:");
		sb.append(this.getLargestPoolSize());
		sb.append("\r\nActiveCount:");
		sb.append(this.getActiveCount());
		sb.append("\r\nCompletedTaskCount:");
		sb.append(this.getCompletedTaskCount());
		sb.append("\r\nTaskCount:");
		sb.append(this.getTaskCount());
		sb.append("\r\n");
		System.out.println(sb.toString());
		logger.info(sb.toString());
		BaseService service = (BaseService)r;
		if (!service.isShutdown()) {
			service.scheduleNextTask();
		}
		if(t == null)
			return;
		t.printStackTrace();
		logger.error("thread run error",t);
	}
}
