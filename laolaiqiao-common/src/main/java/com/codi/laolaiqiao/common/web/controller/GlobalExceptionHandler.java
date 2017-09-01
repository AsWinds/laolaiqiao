package com.codi.laolaiqiao.common.web.controller;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.alibaba.fastjson.JSONObject;
import com.codi.base.exception.BaseAppException;
import com.codi.base.i18n.I18NMgr;
import com.codi.base.util.ExceptionUtil;
import com.codi.base.util.StringUtil;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.web.result.BaseResult;

/**
 *
 * Class used to handle global controller exceptions.
 *
 */
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(
		value = {
			BindException.class
		}
	)
	public BaseResult requestValidationFail(BindException e){
		BindingResult br = e.getBindingResult();
		List<ObjectError> errors = br.getGlobalErrors();
		if (errors == null || CollectionUtils.isEmpty(errors)) {
			return new BaseResult("9001");
        }
		//TODO 如果多个参数校验出错, 只告诉请求端一个参数出错的信息
		ObjectError error = errors.get(0);
		return new BaseResult(error.getCode(), error.getDefaultMessage());
	}

	@ExceptionHandler(
        value = {
            HttpMediaTypeNotAcceptableException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpRequestMethodNotSupportedException.class
        }
    )
    public BaseResult notSupportrd(Exception e) {
        ExceptionUtil.logError(logger, "Unexpected exceptions!!!", e);
        return new BaseResult("9002");
    }

	@ExceptionHandler(
        value = {
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            TypeMismatchException.class
        }
    )
    public BaseResult badRequest(Exception e) {
        ExceptionUtil.logError(logger, "Unexpected exceptions!!!", e);
        return new BaseResult("9001");
    }

	@ExceptionHandler(
        value = {
            ConversionNotSupportedException.class,
            HttpMessageNotWritableException.class,
            MissingPathVariableException.class,
            QueryTimeoutException.class,
            EntityNotFoundException.class,
            LockTimeoutException.class,
            NonUniqueResultException.class,
            NoResultException.class,
            RollbackException.class,
            TransactionRequiredException.class,
            Exception.class
        }
    )
    public BaseResult internalServerError(Exception e) {
        ExceptionUtil.logError(logger, "Unexpected exceptions!!!", e);
        return new BaseResult("9003");
    }

    @ExceptionHandler(
        value = {
            NoHandlerFoundException.class,
            NoSuchRequestHandlingMethodException.class
        }
    )
    public BaseResult notFound(Exception e) {
        ExceptionUtil.logError(logger, "Unexpected exceptions!!!", e);
        return new BaseResult("9004");
    }

    @ExceptionHandler(
        value = {
            OptimisticLockException.class,
            PessimisticLockException.class,
            PersistenceException.class,
            EntityExistsException.class
        }
    )
    public BaseResult dbException(Exception e) {
        ExceptionUtil.logError(logger, "Unexpected exceptions!!!", e);
        BaseResult result = new BaseResult("9005");
        return result;
    }

    @ExceptionHandler(value = {
    		BaseException.class,
    		BaseAppException.class
    })
    public Object handleBaseException(HttpServletRequest request, Exception e) {
        ExceptionUtil.logError(logger, "business exceptions!!!", e);
        BaseResult result = null;
        if (e instanceof BaseException) {
        	BaseException be = (BaseException) e;
        	result = new BaseResult(be.getCode(), be.getMsgArgs());
		}else if (e instanceof BaseAppException) {
			BaseAppException be = (BaseAppException) e;
	        JSONObject jsonObj = new JSONObject();
	        jsonObj.put("success", false);
	        jsonObj.put("errorCode", be.getCode());

	        String desc = be.getDesc();
	        if (StringUtil.isEmpty(desc)) {
	            desc = I18NMgr.getInstance().getValue(be.getCode());
	        }
	        jsonObj.put("errorMessage", desc);
	        return jsonObj;
		}
        return result;
    }


}
