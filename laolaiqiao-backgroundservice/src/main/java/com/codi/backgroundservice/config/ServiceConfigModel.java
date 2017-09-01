package com.codi.backgroundservice.config;

import java.util.List;

public class ServiceConfigModel {
	private String code;
	private String name;
	private String className;
	private int maxThreadNumber;
	private int coreThreadNumber;
	private long intervalMillionSeconds;
	private int startType;
	private List<String> startTime;
	private boolean needStart;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getMaxThreadNumber() {
		return maxThreadNumber;
	}
	public void setMaxThreadNumber(int maxThreadNumber) {
		this.maxThreadNumber = maxThreadNumber;
	}
	public int getCoreThreadNumber() {
		return coreThreadNumber;
	}
	public void setCoreThreadNumber(int coreThreadNumber) {
		this.coreThreadNumber = coreThreadNumber;
	}
	public long getIntervalMillionSeconds() {
		return intervalMillionSeconds;
	}
	public void setIntervalMillionSeconds(long intervalMillionSeconds) {
		this.intervalMillionSeconds = intervalMillionSeconds;
	}
	public int getStartType() {
		return startType;
	}
	public void setStartType(int startType) {
		this.startType = startType;
	}
	public List<String> getStartTime() {
		return startTime;
	}
	public void setStartTime(List<String> startTime) {
		this.startTime = startTime;
	}
	public boolean isNeedStart() {
		return needStart;
	}
	public void setNeedStart(boolean needStart) {
		this.needStart = needStart;
	}
	
}
