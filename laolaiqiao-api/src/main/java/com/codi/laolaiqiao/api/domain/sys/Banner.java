package com.codi.laolaiqiao.api.domain.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.codi.laolaiqiao.common.entity.Domain;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Banner extends Domain {
	
	@Column(nullable = false)
	private String title;
	
	@Column()
	private String url;
	
	@Column
	private String linkUrl;
	
	@Column(nullable = false)
	private int position;
	
	@Column(nullable = false)
	private boolean display;
	
}
