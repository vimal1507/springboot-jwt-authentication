package com.employee.exception;

public class RefreshTokenExpiredException extends RuntimeException {
	
	 public RefreshTokenExpiredException() {
	        super();
	    }
	 
	 public RefreshTokenExpiredException(String message) {
	        super(message);
	    }
}
