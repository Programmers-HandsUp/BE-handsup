package dev.handsup.search.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.search.RedisSearchRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.search.dto.PopularKeywordResponse;
import dev.handsup.search.dto.PopularKeywordsResponse;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
	private final String DIGITAL_DEVICE = "디지털 기기";
	private final int PAGE_NUMBER = 0;
	private final int PAGE_SIZE = 5;

	private final Auction auction = AuctionFixture.auction();

	@Mock
	private AuctionQueryRepository auctionQueryRepository;

	@Mock
	private RedisSearchRepository redisSearchRepository;

	@InjectMocks
	private SearchService searchService;

	@DisplayName("[경매를 정렬, 필터링하여 검색할 수 있다.]")
	@Test
	void searchAuctions() {
		//given
		Auction auction = AuctionFixture.auction(ProductCategory.of(DIGITAL_DEVICE));
		PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword("버즈")
			.build();

		given(auctionQueryRepository.searchAuctions(condition, pageRequest))
			.willReturn(new SliceImpl<>(List.of(auction), pageRequest, true));

		//when
		PageResponse<AuctionResponse> response
			= searchService.searchAuctions(condition, pageRequest);

		//then
		AuctionResponse auctionResponse = response.content().get(0);
		assertThat(auctionResponse).isNotNull();
		verify(redisSearchRepository).increaseSearchCount(condition.keyword());
	}

	@DisplayName("[인기 검색어를 조회할 수 있다]")
	@Test
	void getPopularKeywords() {
		//given
		PopularKeywordResponse popularKeywordResponse1
			= PopularKeywordResponse.of("keyword1", 3);
		PopularKeywordResponse popularKeywordResponse2
			= PopularKeywordResponse.of("keyword2", 1);

		given(redisSearchRepository.getPopularKeywords()).willReturn(
			List.of(popularKeywordResponse1, popularKeywordResponse2));

		//when
		PopularKeywordsResponse response = searchService.getPopularKeywords();

		//then
		assertAll(
			() -> assertThat(response.keywords()).hasSize(2),
			() -> assertThat(response.keywords().get(0).keyword())
				.isEqualTo(popularKeywordResponse1.keyword()),
			() -> assertThat(response.keywords().get(1).keyword())
				.isEqualTo(popularKeywordResponse2.keyword()),
			() -> assertThat(response.keywords().get(0).count())
				.isEqualTo(popularKeywordResponse1.count()),
			() -> assertThat(response.keywords().get(1).count())
				.isEqualTo(popularKeywordResponse2.count())
		);
	}
}