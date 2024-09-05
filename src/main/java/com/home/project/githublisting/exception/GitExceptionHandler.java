package com.home.project.githublisting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GitExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<GitException> handleUsernameNotFoundException(UsernameNotFoundException ex) {

		GitException exception = new GitException(HttpStatus.NOT_FOUND.value(), ex.getMessage());

		return new ResponseEntity<GitException>(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GitException> handleException(Exception ex) {

		GitException exception = new GitException(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

		return new ResponseEntity<GitException>(exception, HttpStatus.BAD_REQUEST);
	}

}
