package com.home.project.githublisting.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8080) // Configures WireMock to run on port 8080
class GitHubControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		// Reset WireMock to ensure clean state for each test
		WireMock.reset();
	}

	@Test
	@DisplayName("Integration test for fetching non-fork repositories and branches")
	public void testGetUserRepositories_Success() throws Exception {

		String responseBodyRepo =
				"[{"
						+ "\"name\": \"test-repo\"," 
						+ "\"owner\": {\"login\": \"validUser\"},"
						+ "\"fork\": false"
				+ "}]";

		String responseBodyBranch =
				"[{"
						+ "\"name\": \"main\","
						+ "\"commit\": {\"sha\": \"abcd1234\"}"
				+ "}]";

		// WireMock setup for GitHub Repositories API
		stubFor(get(urlPathEqualTo("/users/validUser/repos"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
						.withBody(responseBodyRepo)));

		// WireMock setup for GitHub Branches API
		stubFor(get(urlEqualTo("/repos/validUser/test-repo/branches"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
						.withBody(responseBodyBranch)));

		mockMvc.perform(
				get("/api/github/repositories/{username}", "validUser").header(HttpHeaders.ACCEPT, "application/json"))
		.andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("test-repo"))
		.andExpect(jsonPath("$[0].owner.login").value("validUser"))
		.andExpect(jsonPath("$[0].branches[0].name").value("main"))
		.andExpect(jsonPath("$[0].branches[0].commit.sha").value("abcd1234"));

	}

	@Test
	@DisplayName("Integration test for non-existing GitHub user")
	public void testGetUserRepositories_UserNotFound() throws Exception {
		// WireMock setup for GitHub User Not Found (404)
		stubFor(get(urlPathEqualTo("/users/nonExistingUser/repos"))
				.willReturn(aResponse().withStatus(HttpStatus.NOT_FOUND.value())
						.withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
						.withBody("{\"message\": \"Not Found\", \"documentation_url\": \"https://developer.github.com/v3\"}")));

		// Perform the GET request using MockMvc
		mockMvc.perform(get("/api/github/repositories/{username}", "nonExistingUser")
				.header(HttpHeaders.ACCEPT, "application/json"))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
		.andExpect(jsonPath("$.message").value("User not found"));
	}

	@Test
	@DisplayName("Integration test for user with no repositories")
	public void testGetUserRepositories_UserHasNoRepositories() throws Exception {
		// WireMock setup for GitHub API returning an empty list of repositories
		stubFor(get(urlPathEqualTo("/users/userWithNoRepos/repos"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
						.withBody("[]")));

		mockMvc.perform(get("/api/github/repositories/{username}", "userWithNoRepos")
				.header(HttpHeaders.ACCEPT, "application/json"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").isArray())
		.andExpect(jsonPath("$").isEmpty());
	}
}
