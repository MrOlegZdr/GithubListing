package com.home.project.githublisting.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.home.project.githublisting.model.Branch;
import com.home.project.githublisting.model.Commit;
import com.home.project.githublisting.model.Owner;
import com.home.project.githublisting.model.Repository;
import com.home.project.githublisting.service.GitHubService;

import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@WireMockTest(httpPort = 8080)
public class GitHubServiceIntegrationTest {

	@Autowired
	private GitHubService gitHubService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("Integration test for fetching non-fork repositories and branches")
	public void testGetRepositories_Success(WireMockRuntimeInfo wireMockRuntimeInfo) {

		// Set Up mock data for the test
		Branch branch = new Branch("main", new Commit("asdf1234"));
		List<Branch> branches = new ArrayList<Branch>();
		branches.add(branch);

		Owner owner = new Owner("testOwner");
		Repository repo = new Repository("testRepo", owner, false, branches);

		List<Repository> repositories = new ArrayList<Repository>();
		repositories.add(repo);

		JsonNode jsonNodeRepo = objectMapper.valueToTree(repositories);
		JsonNode jsonNodeBranch = objectMapper.valueToTree(branches);

		stubFor(get(urlPathEqualTo("/users/testOwner/repos"))
				.willReturn(ok().withHeader(HttpHeaders.CONTENT_TYPE, "application/json").withJsonBody(jsonNodeRepo)));

		stubFor(get(urlPathEqualTo("/repos/testOwner/testRepo/branches")).willReturn(
				ok().withHeader(HttpHeaders.CONTENT_TYPE, "application/json").withJsonBody(jsonNodeBranch)));

		System.out.println(wireMockRuntimeInfo.getHttpBaseUrl());
		List<Repository> result = gitHubService.getRepositories("testOwner");

		assertNotNull(result);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getName(), "testRepo");
		assertEquals(result.get(0).getOwner().getLogin(), "testOwner");
		assertEquals(result.get(0).isFork(), false);
		assertEquals(result.get(0).getBranches().size(), 1);
		assertEquals(result.get(0).getBranches().get(0).getName(), "main");
		assertEquals(result.get(0).getBranches().get(0).getCommit().getSha(), "asdf1234");
	}
}
