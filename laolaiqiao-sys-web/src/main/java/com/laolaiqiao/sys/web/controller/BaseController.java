package com.laolaiqiao.sys.web.controller;

import com.codi.base.util.DateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Date;

/**
 * Controller基类
 *
 * @date 2016年10月31日 下午4:59:44
 */
public class BaseController {

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {

        binder.registerCustomEditor(Date.class, new DateEditor());
    }
}
