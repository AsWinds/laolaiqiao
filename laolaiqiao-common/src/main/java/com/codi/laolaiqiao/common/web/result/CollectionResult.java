package com.codi.laolaiqiao.common.web.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.domain.Page;

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
public class CollectionResult<T> extends BaseResult {
	
	private Collection<T> objs;
	private Long totalNum;
	private Integer currentPage;
	private Integer totalPages;
	
	public CollectionResult() {
		super(true);
	}
	
	public CollectionResult(Page<T> page){
		super(true);
		this.objs = page.getContent();
		this.totalNum = page.getTotalElements();
		this.currentPage = page.getNumber() + 1;
		this.totalPages = page.getTotalPages();
	}
	
	public CollectionResult(List<T> list){
		super(true);
		this.objs = list;
	}

	/**
	 * 
	 * @return 返回一个不可修改的元素集合, 如果没有元素, 返回一个空的不可修改集合
	 * 
	 * */
	public Collection<T> getObjs() {
		if (objs == null) {
			return Collections.unmodifiableList(new ArrayList<T>(0));
		}
		return Collections.unmodifiableCollection(objs);
	}

}
