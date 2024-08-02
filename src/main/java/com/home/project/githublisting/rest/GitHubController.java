package com.home.project.githublisting.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.project.githublisting.model.Repository;
import com.home.project.githublisting.service.GitHubService;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

	private GitHubService gitHubService;

	@Autowired
	public GitHubController(GitHubService gitHubService) {
		this.gitHubService = gitHubService;
	}

	@GetMapping("/repositories/{username}")
	public ResponseEntity<List<Repository>> getUserRepositories(@PathVariable String username) {
		List<Repository> repositories = gitHubService.getRepositories(username);
		return ResponseEntity.ok(repositories);
	}

}
