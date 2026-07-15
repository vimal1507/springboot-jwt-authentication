package com.employee.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
	
	public RefreshTokenNotFoundException() {
		super();
	}
	
	public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
