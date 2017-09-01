package com.codi.laolaiqiao.api.req;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 必须包含bucket和key
 * 
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QiNiuUploadCallback {
	
	@NotNull
	public String key;
	
	@NotNull
	public String hash;
	
	@NotNull
	public String bucket;
	
	public String fname;
	
	public String ext;
	
	@NotNull
	public Long fsize;

}
