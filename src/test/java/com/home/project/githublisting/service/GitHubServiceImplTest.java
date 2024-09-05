package com.home.project.githublisting.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.home.project.githublisting.model.Branch;
import com.home.project.githublisting.model.Commit;
import com.home.project.githublisting.model.Owner;
import com.home.project.githublisting.model.Repository;
import com.home.project.githublisting.rest.UsernameNotFoundException;

class GitHubServiceImplTest {

	private RestTemplate restTemplate;

	private GitHubServiceImpl gitHubService;

	@BeforeEach
	public void setUp() {

		restTemplate = Mockito.mock(RestTemplate.class);
		gitHubService = new GitHubServiceImpl(restTemplate);

	}

	@Test
	public void testGetRepositories_Success() {

		String userName = "testOwner";
		Owner owner = new Owner(userName);
		Branch[] branches = new Branch[] { new Branch("main", new Commit("asdf1234")) };
		Repository[] repositories = new Repository[] {
				new Repository("repoNonFork", owner, false, Arrays.asList(branches)) };

		Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(Repository[].class))).thenReturn(repositories);
		Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(Branch[].class))).thenReturn(branches);

		List<Repository> result = gitHubService.getRepositories(userName);

		assertNotNull(result);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "repoNonFork");
		assertEquals(result.get(0).getOwner().getLogin(), userName);
		assertEquals(result.get(0).isFork(), false);
		assertEquals(result.get(0).getBranches().size(), 1);
		assertEquals(result.get(0).getBranches().get(0).getName(), "main");
		assertEquals(result.get(0).getBranches().get(0).getCommit().getSha(), "asdf1234");
	}

	@Test
	public void testGetRepositories_OnlyNonForks() {

		String userName = "testOwner";
		Owner owner = new Owner(userName);
		Branch[] branches = new Branch[] { new Branch("main", new Commit("asdf1234")) };
		Repository[] repositories = new Repository[] {
				new Repository("repoNonFork", owner, false, Arrays.asList(branches)),
				new Repository("repoFork", owner, true, new ArrayList<Branch>()) };

		Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(Repository[].class))).thenReturn(repositories);
		Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(Branch[].class))).thenReturn(branches);

		List<Repository> result = gitHubService.getRepositories(userName);

		assertNotNull(result);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "repoNonFork");
		assertEquals(result.get(0).getOwner().getLogin(), userName);
		assertEquals(result.get(0).isFork(), false);
		assertEquals(result.get(0).getBranches().size(), 1);
		assertEquals(result.get(0).getBranches().get(0).getName(), "main");
		assertEquals(result.get(0).getBranches().get(0).getCommit().getSha(), "asdf1234");
	}

	@Test
	public void testGetRepositories_NoBranches() {

		String userName = "testOwner";
		Owner owner = new Owner(userName);
		Branch[] branches = new Branch[] {};
		Repository[] repositories = new Repository[] {
				new Repository("repoNonFork", owner, false, Arrays.asList(branches)) };
		
		Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(Repository[].class))).thenReturn(repositories);
		Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(Branch[].class))).thenReturn(branches);
		
		List<Repository> result = gitHubService.getRepositories(userName);
		
		assertNotNull(result);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "repoNonFork");
		assertEquals(result.get(0).getOwner().getLogin(), userName);
		assertEquals(result.get(0).isFork(), false);
		assertEquals(result.get(0).getBranches().size(), 0);

	}
	
	@Test
	public void testGetRepositories_UsernameNotFound() {

		// Simulate a 404 response from the GitHub API
		Mockito.when(restTemplate.getForObject(anyString(), Mockito.eq(Repository[].class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
			gitHubService.getRepositories("nonExistingUser");
		});

		assertEquals("User not found", exception.getMessage());
	}
	
}
