package com.codi.laolaiqiao.tools.db.init;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.codi.laolaiqiao.biz.BizSpringConfiguration;

/**
 * 
 * 该工具类用于向DB内初始化一些数据
 * 
 * */
@Configuration
@ComponentScan
@Import(value = { BizSpringConfiguration.class })
public class RunDBInit {

	public static void main(String[] args) {
		SpringApplication.run(RunDBInit.class);
	}

}
