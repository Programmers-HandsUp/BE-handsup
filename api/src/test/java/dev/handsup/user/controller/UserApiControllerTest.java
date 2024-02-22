package dev.handsup.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[UserApiController 테스트]")
class UserApiControllerTest extends ApiTestSupport {

	@Autowired
	private UserRepository userRepository;

	private User user = UserFixture.user();

	private JoinUserRequest request = JoinUserRequest.of(
		user.getEmail(),
		user.getPassword(),
		user.getNickname(),
		user.getAddress().getSi(),
		user.getAddress().getGu(),
		user.getAddress().getDong(),
		user.getProfileImageUrl()
	);

	@Test
	@DisplayName("[회원가입 API를 호출하면 회원이 등록되고 회원 ID를 응답한다]")
	void joinUserTest() throws Exception {

		// when
		ResultActions actions = mockMvc.perform(
			post("/api/users")
				.contentType(APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").exists());

		assertThat(userRepository.findByEmail(user.getEmail())).isPresent();
	}

}
