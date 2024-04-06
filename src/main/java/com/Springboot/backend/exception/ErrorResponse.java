package com.Springboot.backend.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

public  class ErrorResponse {
	
	private HttpStatus statusCode;
	
	private String message;
	
	private Date dateTime;

	

	public ErrorResponse(HttpStatus statusCode, String message, Date dateTime) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.dateTime = dateTime;
	}



	public HttpStatus getStatusCode() {
		return statusCode;
	}



	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public Date getDateTime() {
		return dateTime;
	}



	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	

	
	
	
	

}
