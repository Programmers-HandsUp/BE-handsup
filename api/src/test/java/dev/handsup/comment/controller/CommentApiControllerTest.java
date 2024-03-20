package dev.handsup.comment.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.comment.domain.Comment;
import dev.handsup.comment.dto.request.RegisterCommentRequest;
import dev.handsup.comment.exception.CommentErrorCode;
import dev.handsup.comment.repository.CommentRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.CommentFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@DisplayName("[Comment 통합 테스트]")
class CommentApiControllerTest extends ApiTestSupport {

	private final User seller = user;
	private final User bidder = UserFixture.user2();
	private Auction auction;
	private ProductCategory productCategory;
	@Autowired
	private CommentRepository commentRepository;

	@BeforeEach
	void setUp() {
		productCategory = ProductFixture.productCategory("디지털 기기");
		productCategoryRepository.save(productCategory);
		userRepository.saveAll(List.of(seller, bidder));
		auction = auctionRepository.save(AuctionFixture.auction(productCategory));
	}

	@DisplayName("[댓글을 등록할 수 있다.]")
	@Test
	void registerComment() throws Exception {
		RegisterCommentRequest request = RegisterCommentRequest.of("와");

		mockMvc.perform(post("/api/auctions/{auctionId}/comments", auction.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request))
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.writerId").value(seller.getId()))
			.andExpect(jsonPath("$.nickname").value(seller.getNickname()))
			.andExpect(jsonPath("$.profileImageUrl").value(seller.getProfileImageUrl()))
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

	@DisplayName("[댓글을 모두 최신순으로 조회할 수 있다.]")
	@Test
	void getAuctionComments() throws Exception {
		//given
		User seller = userRepository.save(UserFixture.user1());
		Comment comment1 = commentRepository.save(CommentFixture.comment(auction, bidder));
		Comment comment2 = commentRepository.save(CommentFixture.comment(auction, seller));

		//when, then
		mockMvc.perform(get("/api/auctions/{auctionId}/comments", auction.getId())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].writerId").value(seller.getId()))
			.andExpect(jsonPath("$.content[0].nickname").value(seller.getNickname()))
			.andExpect(jsonPath("$.content[0].profileImageUrl").value(seller.getProfileImageUrl()))
			.andExpect(jsonPath("$.content[0].content").value(comment2.getContent()))
			.andExpect(jsonPath("$.content[0].isSeller").value(true))
			.andExpect(jsonPath("$.content[1].writerId").value(bidder.getId()))
			.andExpect(jsonPath("$.content[1].nickname").value(bidder.getNickname()))
			.andExpect(jsonPath("$.content[1].profileImageUrl").value(bidder.getProfileImageUrl()))
			.andExpect(jsonPath("$.content[1].content").value(comment1.getContent()))
			.andExpect(jsonPath("$.content[1].isSeller").value(false));
	}
}