package com.home.project.githublisting.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {

	private String name;
	private Owner owner;
	
	@JsonIgnore
	private boolean fork;
	private List<Branch> branches;

	public Repository() {
	}

	public Repository(String name, Owner owner, boolean fork, List<Branch> branches) {
		this.name = name;
		this.owner = owner;
		this.fork = fork;
		this.branches = branches;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public boolean isFork() {
		return fork;
	}

	public void setFork(boolean fork) {
		this.fork = fork;
	}

	public List<Branch> getBranches() {
		return branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

	@Override
	public String toString() {
		return "Repository [name=" + name + ", owner=" + owner + ", fork=" + fork + ", branches=" + branches + "]";
	}

}
