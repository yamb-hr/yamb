package com.tejko.exceptions;

public class UsernameTakenException extends RuntimeException {

	public UsernameTakenException(String message) {
		super(message);
	}

}