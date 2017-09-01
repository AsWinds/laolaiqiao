package com.laolaiqiao.sys.web.result;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.domain.Page;

import com.codi.laolaiqiao.common.web.result.BaseResult;

/**
 * 
 * 表示一般意义的集合类返回结果
 * 
 * @author hzren
 * @since pangu003
 * 
 * */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysResult extends BaseResult {
	
	private Object result;
	private Long totalNum;
	private Integer currentPage;
	private Integer totalPages;
	
	public SysResult(boolean success) {
		super(success);
	}
	
	public SysResult(Object result){
		super(true);
		this.result = result;
	}
	
	public SysResult(Page<?> page){
		super(true);
		this.result = page.getContent();
		this.totalNum = page.getTotalElements();
		this.currentPage = page.getNumber() + 1;
		this.totalPages = page.getTotalPages();
	}
	
	public SysResult(List<?> list){
		super(true);
		this.result = list;
	}

}
