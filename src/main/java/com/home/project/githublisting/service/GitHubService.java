package com.home.project.githublisting.service;

import java.util.List;

import com.home.project.githublisting.model.Repository;

public interface GitHubService {

	public List<Repository> getRepositories(String username);
}
