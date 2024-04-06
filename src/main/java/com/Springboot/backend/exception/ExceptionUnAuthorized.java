package com.Springboot.backend.exception;

public class ExceptionUnAuthorized extends RuntimeException{
	
	public ExceptionUnAuthorized(String message) {
		super(message);
	}
}
