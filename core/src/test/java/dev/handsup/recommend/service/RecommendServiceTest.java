package dev.handsup.recommend.service;

import static org.assertj.core.api.Assertions.*;
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
import org.springframework.data.domain.Sort;

import dev.handsup.auction.domain.auction_field.TradingLocation;
import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionSearchRepository;
import dev.handsup.auction.repository.product.PreferredProductCategoryRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.AuctionSearchFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.recommend.dto.RecommendAuctionResponse;
import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.user.domain.User;

@DisplayName("[추천 서비스 테스트]")
@ExtendWith(MockitoExtension.class)
class RecommendServiceTest {
	private final PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("북마크수"));
	private final User user = UserFixture.user1();
	private final String DIGITAL_DEVICE = "디지털 기기";
	private final String FASHION = "패션/잡화";
	private final ProductCategory productCategory1 = ProductFixture.productCategory(DIGITAL_DEVICE);
	private final ProductCategory productCategory2 = ProductFixture.productCategory(FASHION);

	@Mock
	private PreferredProductCategoryRepository preferredProductCategoryRepository;

	@Mock
	private AuctionSearchRepository auctionSearchRepository;

	@InjectMocks
	private RecommendService recommendService;

	@DisplayName("[정렬 조건에 따라 추천 경매를 조회할 수 있다.]")
	@Test
	void getRecommendAuctions() {
		//given
		String si = "서울시", gu = "성북구", dong = "동선동";
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,1L,TradingLocation.of(si,gu,dong));
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(3L,3L,TradingLocation.of(si,gu,dong));

		given(auctionSearchRepository.sortAuctionByCriteria(si, gu, dong, pageRequest))
			.willReturn(new SliceImpl<>(List.of(auctionSearch1,auctionSearch2), pageRequest, false));
		//when
		PageResponse<RecommendAuctionResponse> response
			= recommendService.getRecommendAuctionsV2(si, gu, dong, pageRequest);
		//then
		assertThat(response.content()).hasSize(2);
	}

	@DisplayName("[유저 선호 카테고리에 맞는 경매를 북마크 순으로 조회할 수 있다.]")
	@Test
	void getUserPreferredCategoryAuctions() {
		//given
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,DIGITAL_DEVICE,1L);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L,FASHION,2L);


		PreferredProductCategory preferredProductCategory1 = PreferredProductCategory.of(user, productCategory1);
		PreferredProductCategory preferredProductCategory2 = PreferredProductCategory.of(user, productCategory2);

		given(preferredProductCategoryRepository.findByUser(user))
			.willReturn(List.of(preferredProductCategory1, preferredProductCategory2));
		given(auctionSearchRepository.findByProductCategories(List.of(productCategory1.getValue(),productCategory2.getValue()), pageRequest))
			.willReturn(new SliceImpl<>(List.of(auctionSearch1,auctionSearch2), pageRequest, false));

		//when
		PageResponse<RecommendAuctionResponse> response = recommendService.getUserPreferredCategoryAuctionsV2(
			user, pageRequest);

		//then
		assertThat(response.content()).hasSize(2);
	}
}

