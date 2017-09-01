package com.codi.laolaiqiao.api.converter;

import org.springframework.core.convert.converter.Converter;

import com.codi.laolaiqiao.api.domain.Activity;
import com.codi.laolaiqiao.api.result.ActivityResult;

public class ActivityConverter implements Converter<Activity, ActivityResult> {

	@Override
    public ActivityResult convert(Activity source) {
	    return new ActivityResult(source);
    }

}
