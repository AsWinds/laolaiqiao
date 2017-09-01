package com.codi.backgroundservice.threadpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.codi.backgroundservice.config.ServiceConfigModel;

public class SerivceThreadPoolFactory {
	private static Map<String,ServiceThreadPoolExecutor> threadPools;
	private static void initThreadPool(){
		threadPools = new HashMap<String,ServiceThreadPoolExecutor>();
	}
	static{
		initThreadPool();
	}
	/**
	 * 依据给出的服务代码返回服务线程应该放置的线程池
	 * @param businessConfig
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ThreadPoolExecutor getThreadPoolExectuor(ServiceConfigModel serviceConfig){
		if(threadPools == null)
			initThreadPool();
		if(serviceConfig == null)
			return null;		
		if(threadPools.containsKey(serviceConfig.getCode()))
			return threadPools.get(serviceConfig.getCode());
		BlockingQueue queue = new LinkedBlockingQueue(); 
		ServiceThreadPoolExecutor executor = new ServiceThreadPoolExecutor(serviceConfig.getCoreThreadNumber(), serviceConfig.getMaxThreadNumber(), 1, TimeUnit.SECONDS, queue);
		executor.setExecutorCode(serviceConfig.getCode());
		threadPools.put(serviceConfig.getCode(), executor);
		return executor;	
		
	}
}
