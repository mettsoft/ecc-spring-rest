package com.ecc.spring.web;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;

import com.ecc.spring.util.ValidationException;
import com.ecc.spring.util.ValidationUtils;

@ControllerAdvice("com.ecc.spring.rest")
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler({ ValidationException.class })
	protected ResponseEntity<Object> handleValidationException(ValidationException cause, WebRequest request) {
		ModelMap body = new ModelMap();

		List<ObjectError> errors = cause.getAllErrors();
		Object target = cause.getTarget();

		List<String> errorMessages = ValidationUtils.localize(errors, messageSource, Locale.ENGLISH);
		for (int i = 0; i < errorMessages.size(); i++) {
			if (i == errorMessages.size() - 1) {
				logger.warn(errorMessages.get(i), cause);			
			}
			else {
				logger.warn(errorMessages.get(i));
			}
		}
	
		body.addAttribute("errors", errorMessages);
		body.addAttribute("target", target);
		return handleExceptionInternal(cause, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}