package dev.handsup.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.user.dto.request.EmailAvailibilityRequest;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[User 통합 테스트]")
class UserApiControllerTest extends ApiTestSupport {

	private final JoinUserRequest request = JoinUserRequest.of(
		"hello12345@naver.com",
		user.getPassword(),
		user.getNickname(),
		user.getAddress().getSi(),
		user.getAddress().getGu(),
		user.getAddress().getDong(),
		user.getProfileImageUrl(),
		List.of(1L)
	);

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("[[회원가입 API] 회원이 등록되고 회원 ID를 응답한다]")
	void joinUserTest() throws Exception {
		// when
		ResultActions actions = mockMvc.perform(
			post("/api/users")
				.contentType(APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		actions.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.userId").exists());

		assertThat(userRepository.findByEmail(user.getEmail())).isPresent();
	}

	@Test
	@DisplayName("[[이메일 중복 체크 API] 이메일 사용 가능 여부를 응답한다 - 성공]")
	void checkEmailAvailabilitySuccessTest() throws Exception {
		// given
		String existedEmail = user.getEmail();
		String requestEmail = "hello" + existedEmail;
		EmailAvailibilityRequest request = EmailAvailibilityRequest.from(requestEmail);

		// when
		ResultActions actions = mockMvc.perform(
			get("/api/users/check-email")
				.contentType(APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.isAvailable").value(true));
	}

	@Test
	@DisplayName("[[이메일 중복 체크 API] 이메일 사용 가능 여부를 응답한다 - 실패]")
	void checkEmailAvailabilityFailTest() throws Exception {
		// given
		String existedEmail = user.getEmail();
		String requestEmail = existedEmail;
		EmailAvailibilityRequest request = EmailAvailibilityRequest.from(requestEmail);

		// when
		ResultActions actions = mockMvc.perform(
			get("/api/users/check-email")
				.contentType(APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.isAvailable").value(false));
	}
}
