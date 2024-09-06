package com.home.project.githublisting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3713072710590889424L;

	public UsernameNotFoundException(String message) {
		super(message);
	}

	public UsernameNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
