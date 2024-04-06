package com.Springboot.backend.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler( ExceptionBadRequest.class)
	public ResponseEntity<ErrorResponse> badRequestException(ExceptionBadRequest ex){
		ErrorResponse error= new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(), new Date());
		
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(ExceptionForbidden.class)
	public ResponseEntity<ErrorResponse> forbiddenException (ExceptionForbidden ex){
		ErrorResponse error= new ErrorResponse(HttpStatus.FORBIDDEN,ex.getMessage(), new Date());
		
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.FORBIDDEN);
		
	}
	
	@ExceptionHandler( ExceptionUnAuthorized.class)
	public ResponseEntity<ErrorResponse> unAuthorizedException (ExceptionUnAuthorized ex){
		ErrorResponse error= new ErrorResponse(HttpStatus.UNAUTHORIZED,ex.getMessage(), new Date());
		
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.UNAUTHORIZED);
		
	}

}
