package com.home.project.githublisting.exception;

public class GitException {

	private final int status;
	private final String message;

	public GitException(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "GitException [status=" + status + ", message=" + message + "]";
	}

}
