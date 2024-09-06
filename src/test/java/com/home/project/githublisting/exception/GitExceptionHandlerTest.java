package com.home.project.githublisting.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GitExceptionHandlerTest {

	private GitExceptionHandler gitExceptionHandler;

	@BeforeEach
	public void setUp() {

		gitExceptionHandler = new GitExceptionHandler();
	}

	@Test
	public void testHandleUsernameNotFoundException() {

		String expected = "User not found";

		UsernameNotFoundException exception = new UsernameNotFoundException(expected);

		ResponseEntity<GitException> responseEntity = gitExceptionHandler.handleUsernameNotFoundException(exception);

		assertNotNull(responseEntity);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
		assertNotNull(responseEntity.getBody());
		assertEquals(responseEntity.getBody().getMessage(), expected);
		assertEquals(responseEntity.getBody().getStatus(), HttpStatus.NOT_FOUND.value());

	}

	@Test
	public void tesHandleException() {

		UsernameNotFoundException exception = new UsernameNotFoundException("Bad request");

		ResponseEntity<GitException> responseEntity = gitExceptionHandler.handleException(exception);

		assertNotNull(responseEntity);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertNotNull(responseEntity.getBody());
		assertEquals(responseEntity.getBody().getMessage(), "Bad request");
		assertEquals(responseEntity.getBody().getStatus(), HttpStatus.BAD_REQUEST.value());
	}
}
