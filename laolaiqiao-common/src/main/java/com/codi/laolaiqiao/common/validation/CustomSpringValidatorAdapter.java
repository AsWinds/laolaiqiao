package com.codi.laolaiqiao.common.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.metadata.ConstraintDescriptor;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import com.codi.laolaiqiao.common.util.ClassUtil;
import com.codi.laolaiqiao.common.util.StringUtil;

public class CustomSpringValidatorAdapter extends SpringValidatorAdapter {

	public CustomSpringValidatorAdapter(javax.validation.Validator targetValidator) {
	    super(targetValidator);
    }
	
	/**
	 * 
	 * 因为前端暂时不支持对应的字段显示错误信息, 后端暂时只返回一条错误信息给前台
	 * 
	 * 因此只返回ObjectError给异常捕捉层, 异常捕捉层可以根据返回的ObjectError的error message来返回error message
	 * 
	 * */
	@Override
	protected void processConstraintViolations(Set<ConstraintViolation<Object>> violations, Errors errors) {
		for (ConstraintViolation<Object> violation : violations) {
			String fieldName = determineField(violation);
			FieldError fieldError = errors.getFieldError(fieldName);
			Class<?> rootBeanCLass = violation.getRootBeanClass();
			if (fieldError == null || !fieldError.isBindingFailure()) {
				try {
					ConstraintDescriptor<?> cd = violation.getConstraintDescriptor();
					String errorCode = determineErrorCode(cd);
					Object[] errorArgs = getArgumentsForConstraint(errors.getObjectName(), fieldName, cd);
					if (errors instanceof BindingResult) {
						BindingResult bindingResult = (BindingResult) errors;
						String[] errorCodes = bindingResult.resolveMessageCodes(errorCode);
						bindingResult.addError(new ObjectError(
								errors.getObjectName(), errorCodes, errorArgs, formatViolationErrorMsg(rootBeanCLass, fieldName, violation.getMessage())));
					}
					else {
						errors.rejectValue(fieldName, errorCode, errorArgs, violation.getMessage());
					}
				}
				catch (NotReadablePropertyException ex) {
					throw new IllegalStateException("JSR-303 validated property '" + fieldName +
							"' does not have a corresponding accessor for Spring data binding - " +
							"check your DataBinder's configuration (bean property versus direct field access)", ex);
				}
			}
		}
	}
	
	private String formatViolationErrorMsg(Class<?> rootBeanCLass, String fieldName, String msg){
		if (msg.contains("{}")) {
	        MsgParams params = ClassUtil.getAnnotationOnField(rootBeanCLass, fieldName, MsgParams.class);
	        if (params == null) {
	            return msg;
            }
	        String[] originParams = params.value();
	        Object[] args = new Object[originParams.length];
	        System.arraycopy(originParams, 0, args, 0, originParams.length);
	        return StringUtil.fastStringFormat(msg, args);
        }
		return msg;
	}

}
