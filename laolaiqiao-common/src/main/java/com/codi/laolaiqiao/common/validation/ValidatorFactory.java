package com.codi.laolaiqiao.common.validation;

import java.util.Locale;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import com.codi.base.validation.LocaleSpecifiedMessageInterpolator;

public class ValidatorFactory {
	
	public static Validator buildValidator() {
		HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
		configuration.failFast(true).ignoreXmlConfiguration();
		ResourceBundleLocator urbl = new PlatformResourceBundleLocator("CustomValidationMessages");
		ResourceBundleMessageInterpolator interpolator = new ResourceBundleMessageInterpolator(urbl);
		configuration.messageInterpolator(new LocaleSpecifiedMessageInterpolator(interpolator, Locale.CHINESE));
		return configuration.buildValidatorFactory().getValidator();
    }
	
	public static void main(String[] args) {
	    buildValidator();
    }

}
