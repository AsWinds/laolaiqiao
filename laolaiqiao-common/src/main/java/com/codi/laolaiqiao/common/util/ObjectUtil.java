package com.codi.laolaiqiao.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.util.ReflectionUtils;

public class ObjectUtil {

    public final static <T> List<T> newObjs(Class<T> type, List<Object[]> argList){
		if (argList.size() == 0) {
	        return new ArrayList<T>(0);
        }
        @SuppressWarnings("unchecked")
        final Constructor<T> ctor = (Constructor<T>) ReflectionUtils.findConstructor(type, argList.get(0));
        if (ctor == null) {
            throw new IllegalArgumentException("No such accessible constructor on object: "+ type.getName());
        }
		ArrayList<T> result = new ArrayList<T>(argList.size());
		for (Object[] obj : argList) {
	        try {
	            result.add(ctor.newInstance(obj));
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
            	throw new IllegalArgumentException(e);
            }
        }
		return result;
	}

    public final static <T> Page<T> convertPage(Class<T> type, Page<Object[]> page){
    	return page.map(new Converter<Object[], T>(){

			@Override
            public T convert(Object[] source) {
	            try {
	                return ConstructorUtils.invokeConstructor(type, source);
                } catch (NoSuchMethodException | IllegalAccessException
                        | InvocationTargetException | InstantiationException e) {
                	throw new RuntimeException(e);
                }
            }});
	}

}
