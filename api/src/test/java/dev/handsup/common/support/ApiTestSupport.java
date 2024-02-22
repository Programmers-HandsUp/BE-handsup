package dev.handsup.common.support;

import static org.springframework.http.MediaType.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.handsup.auth.dto.request.AuthRequest;
import dev.handsup.auth.dto.response.AuthResponse;
import dev.handsup.fixture.UserFixture;
import dev.handsup.support.DatabaseCleaner;
import dev.handsup.support.DatabaseCleanerExtension;
import dev.handsup.support.TestContainerSupport;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Import(DatabaseCleaner.class)
@ExtendWith(DatabaseCleanerExtension.class)
public abstract class ApiTestSupport extends TestContainerSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected UserRepository userRepository;

	protected static String accessToken;
	protected static String refreshToken;
	protected String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	@PostConstruct
	public void setUpUser() throws Exception {
		// 캐싱해서 단 한번만 호출
		if (accessToken != null && refreshToken != null) {
			return;
		}

		User user = UserFixture.user();
		JoinUserRequest joinUserRequest = JoinUserRequest.of(
			user.getEmail(),
			user.getPassword(),
			user.getNickname(),
			user.getAddress().getSi(),
			user.getAddress().getGu(),
			user.getAddress().getDong(),
			user.getProfileImageUrl()
		);

		mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/users")
				.contentType(APPLICATION_JSON)
				.content(toJson(joinUserRequest))
		);

		AuthRequest authRequest = AuthRequest.of(
			joinUserRequest.email(),
			joinUserRequest.password()
		);
		MvcResult loginResult = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(toJson(authRequest))
		).andReturn();

		String stringLoginResponse = loginResult.getResponse().getContentAsString();
		AuthResponse authResponse = objectMapper.readValue(stringLoginResponse, AuthResponse.class);

		accessToken = authResponse.accessToken();
		refreshToken = authResponse.refreshToken();

		log.info("setUpUser() is finished.");
	}

}
