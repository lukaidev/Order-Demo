package com.demo.web;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.demo.exception.OrderException;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class OrderControllerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exception(Exception e) {
		HttpStatus status = HttpStatus.CONFLICT;
		log.error(e.getLocalizedMessage(), e);
		return new ResponseEntity<>(ErrorResponse.message("Something went wrong"), status);
	}
	
	@ExceptionHandler(OrderException.class)
	public ResponseEntity<ErrorResponse> exception(OrderException e) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		log.warn(e.getLocalizedMessage());
		return new ResponseEntity<>(ErrorResponse.message(e.getLocalizedMessage()), status);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> exception(MethodArgumentNotValidException e) {
		String msg = e.getBindingResult().getFieldError().getDefaultMessage();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		log.warn(msg);
		return new ResponseEntity<>(ErrorResponse.message(msg), status);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> exception(EntityNotFoundException e) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		log.error(e.getLocalizedMessage(), e);
		return new ResponseEntity<>(ErrorResponse.message("Not Found"), status);
	}

}
