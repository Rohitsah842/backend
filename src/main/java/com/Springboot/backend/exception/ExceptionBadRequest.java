package com.Springboot.backend.exception;



public class ExceptionBadRequest extends RuntimeException{
	
	public ExceptionBadRequest(String message) {
		super(message);
	}

}
