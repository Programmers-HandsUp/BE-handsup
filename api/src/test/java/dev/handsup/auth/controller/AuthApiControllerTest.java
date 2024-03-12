package dev.handsup.auth.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auth.dto.request.LoginRequest;
import dev.handsup.auth.dto.response.LoginDetailResponse;
import dev.handsup.common.support.ApiTestSupport;
import jakarta.servlet.http.Cookie;

@DisplayName("[Auth 통합 테스트]")
class AuthApiControllerTest extends ApiTestSupport {

	private LoginRequest loginRequest;

	@BeforeEach
	void setUp() {
		loginRequest = new LoginRequest(user.getEmail(), user.getPassword(), "fcmToken123");
	}

	@Test
	@Transactional
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
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.accessToken").exists())
			.andExpect(cookie().exists("refreshToken"))
			.andExpect(cookie().value("refreshToken", not(emptyOrNullString())));
	}

	@Test
	@Transactional
	@DisplayName("[토큰 재발급 API를 호출하면 새로운 엑세스 토큰이 응답된다]")
	void reIssueAccessTokenTest() throws Exception {
		// given
		LoginDetailResponse loginDetailResponse = authService.login(loginRequest);
		String refreshToken = loginDetailResponse.refreshToken();

		// when
		ResultActions actions = mockMvc.perform(
			post("/api/auth/token")
				.contentType(APPLICATION_JSON)
				.cookie(new Cookie("refreshToken", refreshToken))
		);

		// then
		actions.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(content().string(not(emptyOrNullString())));
	}

	@Test
	@Transactional
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
		actions.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
	}
}
