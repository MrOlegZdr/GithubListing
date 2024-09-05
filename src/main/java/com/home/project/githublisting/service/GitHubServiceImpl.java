package com.home.project.githublisting.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.home.project.githublisting.exception.UsernameNotFoundException;
import com.home.project.githublisting.model.Branch;
import com.home.project.githublisting.model.Repository;

@Service
public class GitHubServiceImpl implements GitHubService {

	@Value("${github.api.url}")
	private String gitHubApiUrl;

	private final RestTemplate restTemplate;

	public GitHubServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Repository> getRepositories(String username) {

		try {

			String url = String.format("%s/users/%s/repos", gitHubApiUrl, username);
			Repository[] repositories = restTemplate.getForObject(url, Repository[].class);

			return Arrays.stream(repositories).filter(repo -> !repo.isFork()).peek(repo -> {
				String branchesUrl = String.format("%s/repos/%s/%s/branches", gitHubApiUrl, username, repo.getName());
				Branch[] branches = restTemplate.getForObject(branchesUrl, Branch[].class);
				repo.setBranches(Arrays.asList(branches));
			}).collect(Collectors.toList());

		} catch (HttpClientErrorException.NotFound e) {
			throw new UsernameNotFoundException("User not found", e);
		}
	}

}
