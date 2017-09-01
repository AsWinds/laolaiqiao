package com.codi.laolaiqiao.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;


/**
 * 
 * util class get class related information
 * 
 * */
public class ClassUtil {
	
	/**
	 * 
	 * 获取指定类指定field上的指定注解
	 * 
	 * */
	public static <T extends Annotation> T getAnnotationOnField(Class<?> rootClass, String fieldName, Class<T> t){
		Field field = FieldUtils.getField(rootClass, fieldName);
		return field.getAnnotation(t);
	}

}
