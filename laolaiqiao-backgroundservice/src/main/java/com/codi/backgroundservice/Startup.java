package com.codi.backgroundservice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codi.backgroundservice.config.ServiceConfigModel;
import com.codi.backgroundservice.config.ServiceConfigUtils;
import com.codi.backgroundservice.service.BaseService;
import com.codi.backgroundservice.threadpool.SerivceThreadPoolFactory;
import com.codi.base.util.Assert;
import com.codi.bus.util.ServiceUtil;
/**
 * 后台服务启动类
 * 在启东时，启动配置的主线程配置，创建对应的服务主线程
 * @author Shangdu Lin
 *
 */
public class Startup {
	
	private static final Logger logger = LoggerFactory.getLogger(Startup.class);
	public static final InetSocketAddress SHUT_DOWN_ADDRESS = new InetSocketAddress("127.0.0.1", 8989);
	
	public static void main(String[] args){
		// 读取所有需要启动的后台服务的配置信息
		List<ServiceConfigModel> serviceConfigs = ServiceConfigUtils.getServiceConfigs();
		String msg = "start service : service count = " + serviceConfigs.size();
		System.out.print(msg);
		logger.info(msg);
		List<ExecutorServicePair> executorServicePairs = new ArrayList<>(serviceConfigs.size());
		for(ServiceConfigModel serviceConfig:serviceConfigs){
			executorServicePairs.add(startServiceThread(serviceConfig));
		}
		
		ServerSocket shutDownServer = null;
		try {
			shutDownServer = new ServerSocket();
			shutDownServer.bind(SHUT_DOWN_ADDRESS);
		} catch (IOException e) {
			logger.error("Start shutdown server on address : " + SHUT_DOWN_ADDRESS.getHostString() + " fail !!!", e);
			System.exit(-1);
			return;
		}
		System.out.println("Start background service successfully.");
		try {
			Socket socket = shutDownServer.accept();
			try {
				doClose(executorServicePairs);
			} catch (Exception e) {
				logger.error("Error happens during close application");
			}
			socket.close();
			shutDownServer.close();
		} catch (IOException e) {
			logger.error("Listen shut down command fail !!!", e);
			return;
		}
		
	}
	private static ExecutorServicePair startServiceThread(ServiceConfigModel serviceConfig){
		// 记录创建主线程的日志, 创建实例
		String msg = "开始获取线程池：" + serviceConfig.getCode() + " " + serviceConfig.getName();
		System.out.println(msg);
		logger.info(msg);
		try{
			ThreadPoolExecutor executor = SerivceThreadPoolFactory.getThreadPoolExectuor(serviceConfig);
			BaseService service = (BaseService)Class.forName(serviceConfig.getClassName()).newInstance();
			service.setServiceConfig(serviceConfig);
			executor.execute(service);
			return new ExecutorServicePair(service, executor);
		}catch(Exception e){	
			// 记录错误日志，进行错误处理
			logger.error("数据同步线程运行错误。",e);
			throw new RuntimeException(e);
		}finally{
			
		}
	}
	
	private static void doClose(Collection<ExecutorServicePair> esps){
		for (ExecutorServicePair esp : esps) {
			esp.baseService.shutDown();
		}
		for (ExecutorServicePair esp : esps) {
			if (!esp.executor.isShutdown()) {
				esp.executor.shutdownNow();
			}
		}
		ServiceUtil.closeApplicationContext();
	}
	
	static class ExecutorServicePair{
		public final BaseService baseService;
		public final ThreadPoolExecutor executor;
		
		public ExecutorServicePair(BaseService baseService, ThreadPoolExecutor executor) {
			Assert.notNull(baseService);
			Assert.notNull(executor);
			this.baseService = baseService;
			this.executor = executor;
		}
	}
}

