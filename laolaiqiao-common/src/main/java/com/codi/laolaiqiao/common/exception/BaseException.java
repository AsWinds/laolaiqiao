package com.codi.laolaiqiao.common.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	private final String code;
	private final Object[] msgArgs;

    /**
     *
     * 参数: code, errorMsg
     *
     * */
    public BaseException(String code, Object... args) {
        this.code = code;
        this.msgArgs = args;

    }

    public BaseException(String code) {
        this.code = code;
        this.msgArgs = new Object[0];
    }

}
