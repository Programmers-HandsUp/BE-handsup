package dev.handsup.auction.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private final int PAGE_NUMBER = 0;
	private final int PAGE_SIZE = 5;
	@Mock
	private AuctionRepository auctionRepository;
	@Mock
	private AuctionQueryRepository auctionQueryRepository;
	@Mock
	private ProductCategoryRepository productCategoryRepository;

	@InjectMocks
	private AuctionService auctionService;

	@Test
	@DisplayName("[경매를 등록할 수 있다.]")
	void registerAuction() {
		// given
		ProductCategory productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		Auction auction = AuctionFixture.auction(productCategory);
		RegisterAuctionRequest registerAuctionRequest =
			RegisterAuctionRequest.of(
				"거의 새상품 버즈 팔아요",
				DIGITAL_DEVICE,
				10000,
				LocalDate.parse("2022-10-18"),
				ProductStatus.NEW.getLabel(),
				PurchaseTime.UNDER_ONE_MONTH.getLabel(),
				"거의 새상품이에요",
				TradeMethod.DELIVER.getLabel(),
				"서울시",
				"성북구",
				"동선동"
			);

		given(productCategoryRepository.findByCategoryValue(DIGITAL_DEVICE))
			.willReturn(Optional.of(productCategory));
		given(auctionRepository.save(any(Auction.class))).willReturn(auction);

		// when
		AuctionResponse auctionResponse = auctionService.registerAuction(registerAuctionRequest);

		// then
		assertAll(
			() -> assertThat(auctionResponse.title()).isEqualTo(registerAuctionRequest.title()),
			() -> assertThat(auctionResponse.tradeMethod()).isEqualTo(registerAuctionRequest.tradeMethod()),
			() -> assertThat(auctionResponse.endDate()).isEqualTo(registerAuctionRequest.endDate()),
			() -> assertThat(auctionResponse.purchaseTime()).isEqualTo(registerAuctionRequest.purchaseTime()),
			() -> assertThat(auctionResponse.productCategory()).isEqualTo(registerAuctionRequest.productCategory())
		);
	}

	@DisplayName("[경매를 정렬, 필터링하여 검색할 수 있다.]")
	@Test
	void searchAuctions() {
	    //given
		Auction auction = AuctionFixture.auction(ProductCategory.of(DIGITAL_DEVICE));
		PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword("버즈")
			.build();

		given(auctionQueryRepository.searchAuctions(condition,pageRequest))
			.willReturn(new SliceImpl<>(List.of(auction),pageRequest,true));

		//when
		PageResponse<AuctionResponse> response
			= auctionService.searchAuctions(condition, pageRequest);

		//then
		AuctionResponse auctionResponse = response.content().get(0);
		Assertions.assertThat(auctionResponse).isNotNull();
	}
}
