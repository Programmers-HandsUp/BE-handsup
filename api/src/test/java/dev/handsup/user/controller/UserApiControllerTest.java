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

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategoryValue;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.PreferredProductCategoryRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ReviewFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.ReviewLabelValue;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.review.repository.ReviewRepository;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.EmailAvailabilityRequest;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.repository.UserRepository;
import dev.handsup.user.repository.UserReviewLabelRepository;

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
	private UserReviewLabelRepository userReviewLabelRepository;
	@Autowired
	private ReviewLabelRepository reviewLabelRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PreferredProductCategoryRepository preferredProductCategoryRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

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

		// when
		ResultActions actions = mockMvc.perform(
			get("/api/users/check-email")
				.param("email", requestEmail)
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

		// when
		ResultActions actions = mockMvc.perform(
			get("/api/users/check-email")
				.param("email", existedEmail)
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.isAvailable").value(false));
	}

	@Test
	@DisplayName("[[유저 리뷰 라벨 조회 API] 유저의 리뷰 라벨이 id 기준 오름차순으로 반환된다]")
	void getUserReviewLabelsTest() throws Exception {
		// given
		ReviewLabel reviewLabel1 = ReviewLabel.from(ReviewLabelValue.MANNER.getDescription());
		ReviewLabel reviewLabel2 = ReviewLabel.from(ReviewLabelValue.CHEAP.getDescription());
		reviewLabelRepository.saveAll(
			List.of(reviewLabel1, reviewLabel2)
		);
		userReviewLabelRepository.save(UserReviewLabel.of(reviewLabel1, user));
		userReviewLabelRepository.save(UserReviewLabel.of(reviewLabel2, user));

		// when & then
		mockMvc.perform(get("/api/users/{userId}/reviews/labels", user.getId())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()").value(2))
			.andExpect(jsonPath("$.content[0].userReviewLabelId").value(1L))
			.andExpect(jsonPath("$.content[0].reviewLabelId").value(reviewLabel1.getId()))
			.andExpect(jsonPath("$.content[0].userId").value(user.getId()))
			.andExpect(jsonPath("$.content[0].count").value(1))
			.andExpect(jsonPath("$.content[1].userReviewLabelId").value(2L))
			.andExpect(jsonPath("$.content[1].reviewLabelId").value(reviewLabel2.getId()))
			.andExpect(jsonPath("$.content[1].userId").value(user.getId()))
			.andExpect(jsonPath("$.content[1].count").value(1));
	}

	@Test
	@DisplayName("[[유저 리뷰 조회 API] 유저의 리뷰가 생성된 시간 기준 내림차순으로 반환된다]")
	void getUserReviewsTest() throws Exception {
		// given
		User seller = UserFixture.user1();
		User writer = UserFixture.user2();

		userRepository.saveAll(List.of(seller, writer));
		Auction auction = AuctionFixture.auction(seller);
		productCategoryRepository.save(auction.getProduct().getProductCategory());
		auctionRepository.save(auction);

		// review2 가 더 최근에 생성
		Review review1 = ReviewFixture.review("굿입니다 좋아요", auction, writer);
		Review review2 = ReviewFixture.review("나쁘지 않았어요", auction, writer);
		reviewRepository.saveAll(List.of(review1, review2));

		// when & then
		mockMvc.perform(get("/api/users/{userId}/reviews", user.getId())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()").value(2))
			.andExpect(jsonPath("$.content[0].writerNickName").value(writer.getNickname()))
			.andExpect(jsonPath("$.content[0].content").value(review2.getContent()))
			.andExpect(jsonPath("$.content[1].writerNickName").value(writer.getNickname()))
			.andExpect(jsonPath("$.content[1].content").value(review1.getContent()));
	}

	@Test
	@DisplayName("[사용자의 프로필이 반환된다]")
	void getUserProfile() throws Exception {
		// given
		ProductCategory productCategory1 = ProductCategory.from(ProductCategoryValue.BEAUTY_COSMETICS.toString());
		ProductCategory productCategory2 = ProductCategory.from(ProductCategoryValue.BOOKS.toString());

		productCategoryRepository.saveAll(
			List.of(productCategory1, productCategory2)
		);

		preferredProductCategoryRepository.deleteAll();
		preferredProductCategoryRepository.saveAll(
			List.of(
				PreferredProductCategory.of(user, productCategory1),
				PreferredProductCategory.of(user, productCategory2)
			)
		);

		// when, then
		mockMvc.perform(get("/api/users/{userId}/profiles", user.getId())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.profileImageUrl").value(user.getProfileImageUrl()))
			.andExpect(jsonPath("$.nickname").value(user.getNickname()))
			.andExpect(jsonPath("$.dong").value(user.getAddress().getDong()))
			.andExpect(jsonPath("$.preferredProductCategories.size()").value(2))
			.andExpect(jsonPath("$.preferredProductCategories[0]")
				.value(ProductCategoryValue.BEAUTY_COSMETICS.toString()))
			.andExpect(jsonPath("$.preferredProductCategories[1]")
				.value(ProductCategoryValue.BOOKS.toString()))
			.andExpect(jsonPath("$.score").value(user.getScore()));
	}
}
