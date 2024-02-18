package dev.handsup.auction.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.AuctionResponse;
import dev.handsup.auction.dto.RegisterAuctionRequest;
import dev.handsup.auction.repository.AuctionRepository;
import dev.handsup.auction.repository.ProductCategoryRepository;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

	private final String DIGITAL_DEVICE = "디지털 기기";
	@Mock
	private AuctionRepository auctionRepository;
	@Mock
	private ProductCategoryRepository productCategoryRepository;

	@InjectMocks
	private AuctionService auctionService;

	@Test
	@DisplayName("경매글을 등록할 수 있다.")
	void registerAuction() {
		// given
		ProductCategory productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		Auction auction = AuctionFixture.auction();
		RegisterAuctionRequest registerAuctionRequest =
			RegisterAuctionRequest.builder()
				.title("거의 새상품 버즈 팔아요")
				.tradeMethod(TradeMethod.DIRECT.getLabel())
				.purchaseTime(PurchaseTime.UNDER_ONE_MONTH.getLabel())
				.productCategory(DIGITAL_DEVICE)
				.productStatus(ProductStatus.NEW.getLabel())
				.endDate(LocalDate.parse("2022-10-18"))
				.description("거의 새상품이에요")
				.initPrice(10000)
				.si("서울시")
				.gu("성북구")
				.dong("동선동")
				.build();

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
}
