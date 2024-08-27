package com.home.project.githublisting.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.home.project.githublisting.model.Branch;
import com.home.project.githublisting.model.Commit;
import com.home.project.githublisting.model.Owner;
import com.home.project.githublisting.model.Repository;
import com.home.project.githublisting.service.GitHubService;

@WebMvcTest(GitHubController.class)
class GitHubControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GitHubService gitHubService;

	private List<Repository> repositories;

	@BeforeEach
	public void setUp() {
		// Set Up mock data for the test
		Branch branch = new Branch("main", new Commit("asdf1234"));
		List<Branch> branchs = new ArrayList<Branch>();
		branchs.add(branch);

		Owner owner = new Owner("testOwner");
		Repository repo = new Repository("testRepo", owner, false, branchs);

		repositories = new ArrayList<Repository>();
		repositories.add(repo);

		// Mocking the service method
		Mockito.when(gitHubService.getRepositories(anyString())).thenReturn(repositories);
	}

	@Test
	public void testGetUserRepositories_Success() throws Exception {
		String userName = "testOwner";
		mockMvc.perform(MockMvcRequestBuilders.get("/api/github/repositories/{username}", userName)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("testRepo"))
				.andExpect(jsonPath("$[0].owner.login").value("testOwner"))
				.andExpect(jsonPath("$[0].branches[0].name").value("main"))
				.andExpect(jsonPath("$[0].branches[0].commit.sha").value("asdf1234"));
	}

	@Test
	public void testGetUserRepositories_UserNotFound() throws Exception {
		// Setup the service to throw UsernameNotFoundException
		String userName = "nonExistingUser";
		Mockito.when(gitHubService.getRepositories(userName))
				.thenThrow(new UsernameNotFoundException("User not found", null));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/github/repositories/{username}", userName)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").value("User not found"));

	}

}
