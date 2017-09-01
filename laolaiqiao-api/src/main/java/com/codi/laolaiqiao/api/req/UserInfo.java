package com.codi.laolaiqiao.api.req;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;


@Data
public class UserInfo {
	
    /**
     * 姓名
     * */
	@NotBlank
	@Length(min = 0, max = 10)
    private String name;
	
    /**
     * 地址
     */
	@Length(max = 100)
    private String address;
    
    /**
     * 省, 直辖市
     * */
	@Length(max = 20)
    private String province;
    
    /**
     * 城市
     * */
	@Length(max = 20)
    private String city;
    
    /**
     * 区
     * */
	@Length(max = 20)
    private String district;
    
    /**
     * 街道
     * */
    @NotBlank
    @Length(max = 20)
    private String street;

}
