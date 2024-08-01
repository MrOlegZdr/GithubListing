package com.home.project.githublisting.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {

	private String name;
	private Commit commit;

	public Branch() {
	}

	public Branch(String name, Commit commit) {
		this.name = name;
		this.commit = commit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	@Override
	public String toString() {
		return "Branch [name=" + name + ", commit=" + commit + "]";
	}

}
