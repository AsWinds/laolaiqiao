package com.codi.backgroundservice.service;

import com.codi.backgroundservice.config.ServiceConfigModel;

public interface IBaseService extends Runnable {
	ServiceConfigModel getServiceConfig();
	void setServiceConfig(ServiceConfigModel serviceConfig);	
	void scheduleNextTask();
	void shutDown();
}
