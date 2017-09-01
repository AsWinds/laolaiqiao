package com.codi.laolaiqiao.sys.biz;

import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SysBizSpringConfiguration {

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(2);
		taskScheduler.setRemoveOnCancelPolicy(true);
		taskScheduler.setAwaitTerminationSeconds(3);
		taskScheduler.setThreadNamePrefix("SYS_Task-");
		taskScheduler.setWaitForTasksToCompleteOnShutdown(false);
		taskScheduler.afterPropertiesSet();
		return taskScheduler;
	}

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return taskScheduler().getScheduledExecutor();
	}

}
