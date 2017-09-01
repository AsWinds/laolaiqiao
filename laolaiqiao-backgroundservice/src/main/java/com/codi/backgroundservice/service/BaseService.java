package com.codi.backgroundservice.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.codi.backgroundservice.config.ServiceConfigModel;
import com.codi.backgroundservice.threadpool.SerivceThreadPoolFactory;
import com.codi.backgroundservice.utils.DateUtil;

public abstract class BaseService implements IBaseService{

	private ServiceConfigModel serviceConfig;
	private Timer timer = new Timer();
	private volatile boolean shutdown = false;

	@Override
	public ServiceConfigModel getServiceConfig() {
		return serviceConfig;
	}

	@Override
	public void setServiceConfig(ServiceConfigModel serviceConfig) {
		this.serviceConfig = serviceConfig;
	}
	
	@Override
	public void scheduleNextTask(){

		Date date = getNextDate();
		if(date != null) {
			// 等待间隔时间 goto 开始
			Logger logger = Logger.getLogger(BaseService.class);
			System.out.println("为同步业务["+this.getServiceConfig().getCode()+"]设置下次启动时间：" + date);
			logger.info("为同步业务["+this.getServiceConfig().getCode()+"]设置下次启动时间：" + date);
			timer.schedule(new LoopTask(), date);
		}
		
	}
	
	/**
	 * 下一次任务
	 * @author Administrator
	 *
	 */
	public class LoopTask extends TimerTask {

		@Override
		public void run() {
			try {
				ThreadPoolExecutor executor = SerivceThreadPoolFactory.getThreadPoolExectuor(getServiceConfig());
				BaseService service = (BaseService)Class.forName(getServiceConfig().getClassName()).newInstance();
				service.setServiceConfig(getServiceConfig());
				executor.execute(service);
			} catch (Exception e) {
				Logger.getLogger(this.getClass()).error("循环启动业务失败",e);
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 获取下次启动时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected Date getNextDate() {
		Date date = null;
		try {
			Calendar cal = Calendar.getInstance();
			int startType = this.getServiceConfig().getStartType();
			if(startType == ServiceConstants.BUSINESS_START_TYPE_FIXEDTIME) {
				List<String> times = this.getServiceConfig().getStartTime();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);
				int index = -1;
				for (int i = 0; i < times.size(); i++) {
					Date d = DateUtil.parseDate(times.get(i), DateUtil.FORMAT_HH_MM);
					if(hour > d.getHours() || (hour==d.getHours() && minute>=d.getMinutes())) {
						continue;
					}
					index = i;
					break;
				}
				
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				if(index != -1) {
					Date d = DateUtil.parseDate(times.get(index), DateUtil.FORMAT_HH_MM);
					hour = d.getHours();
					minute = d.getMinutes();
					cal.set(Calendar.HOUR_OF_DAY, hour);
					cal.set(Calendar.MINUTE, minute);
					date = cal.getTime();
				}else {
					Date d = DateUtil.parseDate(times.get(0), DateUtil.FORMAT_HH_MM);
					hour = d.getHours();
					minute = d.getMinutes();
					cal.set(Calendar.HOUR_OF_DAY, hour);
					cal.set(Calendar.MINUTE, minute);
					date = cal.getTime();
					date = DateUtil.addDay(date, 1);
				}
			}else if(startType == ServiceConstants.BUSINESS_START_TYPE_LOOP) {
				long intervalmillionseconds = serviceConfig.getIntervalMillionSeconds();
				long millis = cal.getTimeInMillis();
				cal.setTimeInMillis(millis + intervalmillionseconds);
				date = cal.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Logger.getLogger(this.getClass()).error("业务启动线程运行错误",e);
		}
		return date;
	}
	
	@Override
	public void shutDown() {
		timer.cancel();
		this.shutdown = true;
	}
	
	public boolean isShutdown(){
		return shutdown;
	}
	
}
