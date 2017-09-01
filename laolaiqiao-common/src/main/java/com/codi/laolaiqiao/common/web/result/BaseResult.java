package com.codi.laolaiqiao.common.web.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.codi.laolaiqiao.common.util.MsgUtil;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class BaseResult {

    private Boolean success;
    private String errorCode;
    private String errorMessage;
    
    protected BaseResult() {
    }

    public BaseResult(boolean success){
    	this.success = success;
    }

    /**
     *
     * 创建一个BaseResult, 指定其errorCode和errorMessage
     *
     * */
    public BaseResult(String errorCode, String errorMessage){
    	this.success = false;
    	this.errorCode = errorCode;
    	this.errorMessage = errorMessage;
    }

    /**
     * 用于根据捕获的 com.codi.laolaiqiao.common.exception.BaseException 转化为 BaseResult 结果
     *
     * 根据errorCode和args生成返回结果
     * errorCode用于获取message文件里定义的错误信息
     * args 用于格式化该错误信息, 该参数可不传
     *
     * */
    public BaseResult(String errorCode, Object ...args){
        this.success = false;
        this.errorCode = errorCode;
        setErrorMessage(errorCode, args);
    }

    private void setErrorMessage(String errorCode, Object ...args) {
    	this.errorMessage = MsgUtil.getMsg(errorCode, args);
    }
}
