package com.example.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class BeanConfig {

	@Autowired
	MessageSource messageSource;

	@Bean
	public LocalValidatorFactoryBean validator() {

		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource);
		return validator;
	}

}
