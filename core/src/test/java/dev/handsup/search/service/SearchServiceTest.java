package dev.handsup.search.service;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
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
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.search.RedisSearchRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.AuctionFixture;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
	private final String DIGITAL_DEVICE = "디지털 기기";
	private final int PAGE_NUMBER = 0;
	private final int PAGE_SIZE = 5;

	private final Auction auction = AuctionFixture.auction();

	@Mock
	private AuctionRepository auctionRepository;
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
		Assertions.assertThat(auctionResponse).isNotNull();
		verify(redisSearchRepository).increaseSearchCount(condition.keyword());
	}
}