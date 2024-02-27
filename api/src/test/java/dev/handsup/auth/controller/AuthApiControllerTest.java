package dev.handsup.auth.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import dev.handsup.auth.dto.request.TokenReIssueRequest;
import dev.handsup.auth.dto.request.LoginRequest;
import dev.handsup.auth.dto.response.LoginDetailResponse;
import dev.handsup.auth.service.AuthService;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.service.UserService;

@DisplayName("[AuthApiController 테스트]")
class AuthApiControllerTest extends ApiTestSupport {

	private final User user = UserFixture.user(1L);
	@Autowired
	private UserService userService;
	@Autowired
	private AuthService authService;
	private LoginRequest loginRequest;

	@BeforeEach
	void setUp() {
		JoinUserRequest joinUserRequest = JoinUserRequest.of(
			user.getEmail(),
			user.getPassword(),
			user.getNickname(),
			user.getAddress().getSi(),
			user.getAddress().getGu(),
			user.getAddress().getDong(),
			user.getProfileImageUrl()
		);
		userService.join(joinUserRequest);
		loginRequest = new LoginRequest(user.getEmail(), user.getPassword());
	}

	@Test
	@DisplayName("[로그인 API를 호출하면 토큰이 응답된다]")
	void loginTest() throws Exception {
		// when
		ResultActions actions = mockMvc.perform(
			post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(toJson(loginRequest))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists())
			.andExpect(jsonPath("$.refreshToken").exists());
	}

	@Test
	@DisplayName("[토큰 재발급 API를 호출하면 새로운 엑세스 토큰이 응답된다]")
	void reIssueAccessTokenTest() throws Exception {
		// given
		LoginDetailResponse loginDetailResponse = authService.login(loginRequest);
		String refreshToken = loginDetailResponse.refreshToken();
		TokenReIssueRequest tokenReIssueRequest = new TokenReIssueRequest(refreshToken);

		// when
		ResultActions actions = mockMvc.perform(
			post("/api/auth/token")
				.contentType(APPLICATION_JSON)
				.content(toJson(tokenReIssueRequest))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(content().string(not(emptyOrNullString())));
	}

	@Test
	@DisplayName("[로그아웃 API를 호출하면 200 OK 응답이 반환된다]")
	void logoutTest() throws Exception {
		// given
		LoginDetailResponse loginDetailResponse = authService.login(loginRequest);
		String accessToken = loginDetailResponse.accessToken();

		// when
		ResultActions actions = mockMvc.perform(
			post("/api/auth/logout")
				.header(AUTHORIZATION, "Bearer " + accessToken)
		);

		// then
		actions.andExpect(status().isOk());
	}
}
