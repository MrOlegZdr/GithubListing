package com.home.project.githublisting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {

	private String sha;

	public Commit() {
	}

	public Commit(String sha) {
		this.sha = sha;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	@Override
	public String toString() {
		return "Commit [sha=" + sha + "]";
	}

}
