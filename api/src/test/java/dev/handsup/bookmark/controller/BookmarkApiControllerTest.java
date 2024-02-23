package dev.handsup.bookmark.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.bookmark.exception.BookmarkErrorCode;
import dev.handsup.bookmark.repository.BookmarkRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BookmarkFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

class BookmarkApiControllerTest extends ApiTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private final ProductCategory productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
	private final Auction auction = AuctionFixture.auction(productCategory);
	private final User user = UserFixture.user();
	@Autowired
	private BookmarkRepository bookmarkRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		productCategoryRepository.save(productCategory);
		userRepository.save(user);
		auctionRepository.save(auction);
	}

	@DisplayName("[북마크를 추가할 수 있다.]")
	@Test
	void addBookmark() throws Exception {
		mockMvc.perform(post("/api/auctions/bookmarks/{auctionId}", auction.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.bookmarkCount").value(1));
	}

	@DisplayName("[북마크 추가 시 북마크가 존재하면 예외가 발생한다.]")
	@Test
	void addBookmark_fails() throws Exception {
		Bookmark bookmark = BookmarkFixture.bookmark(user, auction);
		bookmarkRepository.save(bookmark);

		mockMvc.perform(post("/api/auctions/bookmarks/{auctionId}", auction.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, accessToken))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message")
				.value(BookmarkErrorCode.ALREADY_EXISTS_BOOKMARK.getMessage()))
			.andExpect(jsonPath("$.code")
				.value(BookmarkErrorCode.ALREADY_EXISTS_BOOKMARK.getCode()));
	}

	@DisplayName("[북마크를 삭제할 수 있다.]")
	@Test
	void deleteBookmark() throws Exception {
		Bookmark bookmark = BookmarkFixture.bookmark(user, auction);
		bookmarkRepository.save(bookmark);

		mockMvc.perform(delete("/api/auctions/bookmarks/{auctionId}", auction.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.bookmarkCount").value(auction.getBookmarkCount() - 1));
	}

	@DisplayName("[북마크 삭제 시 북마크가 존재하지 않으면 예외가 발생한다.]")
	@Test
	void deleteBookmark_fails() throws Exception {
		mockMvc.perform(delete("/api/auctions/bookmarks/{auctionId}", auction.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, accessToken))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(BookmarkErrorCode.NOT_FOUND_BOOKMARK.getMessage()))
			.andExpect(jsonPath("$.code").value(BookmarkErrorCode.NOT_FOUND_BOOKMARK.getCode()));
	}

	@DisplayName("[북마크가 없으면 북마크 여부 조회 시 false를 반환한다.]")
	@Test
	void getBookmarkStatusFalse() throws Exception {
		mockMvc.perform(get("/api/auctions/bookmarks/{auctionId}", auction.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isBookmarked").value(false));
	}

	@DisplayName("[북마크가 존재하면 북마크 여부 조회 시 true를 반환한다.]")
	@Test
	void getBookmarkStatusTrue() throws Exception {
		Bookmark bookmark = BookmarkFixture.bookmark(user, auction);
		bookmarkRepository.save(bookmark);
		mockMvc.perform(get("/api/auctions/bookmarks/{auctionId}", auction.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isBookmarked").value(true));
	}

	@DisplayName("[북마크한 경매를 모두 조회할 수 있다.]")
	@Test
	void findUserBookmarks() throws Exception {
		Auction auction2 = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction2);

		Bookmark bookmark1 = BookmarkFixture.bookmark(user, auction);
		Bookmark bookmark2 = BookmarkFixture.bookmark(user, auction2);
		bookmarkRepository.saveAll(List.of(bookmark1, bookmark2));

		mockMvc.perform(get("/api/auctions/bookmarks")
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].auctionId").value(auction.getId()))
			.andExpect(jsonPath("$.content[0].title").value(auction.getTitle()))
			.andExpect(jsonPath("$.content[0].auctionStatus").value(auction.getStatus().getLabel()))
			.andExpect(jsonPath("$.content[0].createdDate").value(auction.getCreatedAt().toLocalDate().toString()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auction2.getId()));
	}
}