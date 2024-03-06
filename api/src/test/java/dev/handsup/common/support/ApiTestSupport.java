package dev.handsup.common.support;

import static org.springframework.http.MediaType.*;

import java.util.Arrays;
import java.util.List;

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

import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategoryValue;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.auth.dto.request.LoginRequest;
import dev.handsup.auth.dto.response.LoginSimpleResponse;
import dev.handsup.auth.exception.AuthErrorCode;
import dev.handsup.auth.service.AuthService;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.fixture.UserFixture;
import dev.handsup.support.DatabaseCleaner;
import dev.handsup.support.DatabaseCleanerExtension;
import dev.handsup.support.TestContainerSupport;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Import(DatabaseCleaner.class)
@ExtendWith(DatabaseCleanerExtension.class)
public abstract class ApiTestSupport extends TestContainerSupport {

	protected final User user = UserFixture.user();
	protected String accessToken;
	protected String refreshToken;

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	protected UserRepository userRepository;
	@Autowired
	protected AuctionRepository auctionRepository;
	@Autowired
	protected AuthService authService;
	@Autowired
	protected ProductCategoryRepository productCategoryRepository;

	protected String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	@PostConstruct
	public void setUpUser() throws Exception {
		// 캐싱해서 단 한번만 호출
		if (accessToken != null && refreshToken != null) {
			return;
		}

		JoinUserRequest joinUserRequest = JoinUserRequest.of(
			user.getEmail(),
			user.getPassword(),
			user.getNickname(),
			user.getAddress().getSi(),
			user.getAddress().getGu(),
			user.getAddress().getDong(),
			user.getProfileImageUrl(),
			List.of(1L)        // 선호 카테고리 id
		);

		productCategoryRepository.save(ProductCategory.of(ProductCategoryValue.BEAUTY_COSMETICS.getLabel()));

		mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/users")
				.contentType(APPLICATION_JSON)
				.content(toJson(joinUserRequest))
		);

		LoginRequest loginRequest = LoginRequest.of(
			user.getEmail(),
			user.getPassword()
		);

		MvcResult loginResult = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(toJson(loginRequest))
		).andReturn();

		Cookie[] cookies = loginResult.getResponse().getCookies();

		String refreshTokenOfCookie = Arrays.stream(cookies)
			.filter(cookie -> "refreshToken".equals(cookie.getName()))
			.findFirst()
			.map(Cookie::getValue)
			.orElse(null);

		if (refreshTokenOfCookie != null) {
			refreshToken = refreshTokenOfCookie;
		} else {
			throw new NotFoundException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN_IN_RESPONSE);
		}

		String stringLoginSimpleResponse = loginResult.getResponse().getContentAsString();
		LoginSimpleResponse loginSimpleResponse = objectMapper.readValue(
			stringLoginSimpleResponse,
			LoginSimpleResponse.class
		);

		accessToken = loginSimpleResponse.accessToken();

		log.info("setUpUser() is success.");
	}

}
