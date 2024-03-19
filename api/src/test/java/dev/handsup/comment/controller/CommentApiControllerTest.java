package dev.handsup.comment.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.comment.dto.request.RegisterCommentRequest;
import dev.handsup.comment.exception.CommentErrorCode;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.user.domain.User;

@DisplayName("[Comment 통합 테스트]")
class CommentApiControllerTest extends ApiTestSupport {

	private Auction auction;
	private User writer = user;
	private ProductCategory productCategory;

	@BeforeEach
	void setUp() {
		productCategory = ProductFixture.productCategory("디지털 기기");
		productCategoryRepository.save(productCategory);
		writer = userRepository.save(writer);
		auction = auctionRepository.save(AuctionFixture.auction(productCategory));
	}

	//auction.updateAuctionStatusTrading();
	@DisplayName("[댓글을 등록할 수 있다.]")
	@Test
	void registerComment() throws Exception {
		RegisterCommentRequest request = RegisterCommentRequest.of("와");

		mockMvc.perform(post("/api/auctions/{auctionId}/comments", auction.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request))
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.writerId").value(writer.getId()))
			.andExpect(jsonPath("$.nickname").value(writer.getNickname()))
			.andExpect(jsonPath("$.profileImageUrl").value(writer.getProfileImageUrl()))
			.andExpect(jsonPath("$.content").value(request.content()))
			.andExpect(jsonPath("$.isSeller").value(true));
	}

	@Transactional
	@DisplayName("[거래 중인 경매에는 댓글을 달 수 없다.]")
	@Test
	void registerComment_fails() throws Exception {
		RegisterCommentRequest request = RegisterCommentRequest.of("와");
		auction.updateAuctionStatusTrading();

		mockMvc.perform(post("/api/auctions/{auctionId}/comments", auction.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request))
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(CommentErrorCode.COMMENT_NOT_AVAIL_AUCTION.getCode()));
	}
}