package com.codi.laolaiqiao.api.req;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class QiNiuPfopCallback {
	
	public String id;
	
	public String code;
	
	public String desc;
	
	@NotNull
	public String inputKey;
	
	@NotNull
	public String inputBucket;
	
	public List<QiNiuPfopCallbackItem> items;
	
	public QiNiuPfopCallback(String bucket, String key){
		this.inputBucket = bucket;
		this.inputKey = key;
	}

}
