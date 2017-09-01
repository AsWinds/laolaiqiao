package com.codi.laolaiqiao.common.web.result;

public class SingleDataResult<T> extends BaseResult {
	
	private final T data;

	/**
	 * 
	 * 创建一个不包含data的result
	 * 
	 * */
	public SingleDataResult(boolean success) {
		super(success);
		this.data = null;
	}
	
	/**
	 * 
	 * 创建一个包含data的result, success= true
	 * 
	 * */
	public SingleDataResult(T data){
		super(true);
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
}
