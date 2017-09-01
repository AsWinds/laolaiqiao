package com.codi.laolaiqiao.api.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.codi.laolaiqiao.common.entity.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name", "userId"})
	}
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserAlbum extends Domain {
	
	/**
	 * 相册名称
	 * */
	private String name;
	
	/**
	 * 用户ID
	 * */
	private Long userId;

}
