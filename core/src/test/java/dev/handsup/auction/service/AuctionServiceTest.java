package dev.handsup.auction.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.auction.AuctionSearchRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.search.dto.AuctionSearchMapper;

@DisplayName("[경매 서비스 테스트]")
@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {
	private final String DIGITAL_DEVICE = "디지털 기기";
	private final ProductCategory productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
	private final Auction auction = AuctionFixture.auction();

	@Mock
	private AuctionRepository auctionRepository;

	@Mock
	private ProductCategoryRepository productCategoryRepository;

	@Mock
	private AuctionSearchRepository auctionSearchRepository;

	@InjectMocks
	private AuctionService auctionService;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(auction, "createdAt", LocalDateTime.now());
	}

	@Test
	@DisplayName("[경매를 등록할 수 있다.]")
	void registerAuction() {
		// given
		RegisterAuctionRequest request =
			RegisterAuctionRequest.of(
				"거의 새상품 버즈 팔아요",
				DIGITAL_DEVICE,
				10000,
				LocalDate.parse("2022-10-18"),
				ProductStatus.NEW.getLabel(),
				PurchaseTime.UNDER_ONE_MONTH.getLabel(),
				"거의 새상품이에요",
				TradeMethod.DELIVER.getLabel(),
				List.of("image.jpg"),
				"서울시",
				"성북구",
				"동선동"
			);
		AuctionSearch auctionSearch = AuctionSearchMapper.toAuctionSearch(auction);

		given(productCategoryRepository.findByValue(DIGITAL_DEVICE))
			.willReturn(Optional.of(productCategory));
		given(auctionRepository.save(any(Auction.class))).willReturn(auction);
		given(auctionSearchRepository.save(any(AuctionSearch.class))).willReturn(auctionSearch);

		// when
		AuctionDetailResponse response = auctionService.registerAuction(request, UserFixture.user1());
		// then
		assertAll(
			() -> assertThat(response.title()).isEqualTo(request.title()),
			() -> assertThat(response.tradeMethod()).isEqualTo(request.tradeMethod()),
			() -> assertThat(response.endDate()).isEqualTo(request.endDate().atStartOfDay().toString()),
			() -> assertThat(response.purchaseTime()).isEqualTo(request.purchaseTime()),
			() -> assertThat(response.productCategory()).isEqualTo(request.productCategory())
		);
	}

	@DisplayName("[경매 상세정보를 조회할 수 있다.]")
	@Test
	void getAuctionDetail() {
		//given
		given(auctionRepository.findById(anyLong())).willReturn(Optional.of(auction));
		//when
		AuctionDetailResponse response = auctionService.getAuctionDetail(auction.getId());
		//then
		assertThat(response.auctionId()).isEqualTo(auction.getId());
	}

	@DisplayName("[존재하지 않는 경매 아이디로 조회 시 예외를 반환한다.]")
	@Test
	void getAuctionDetail_fails() {
		//given
		Long auctionId = auction.getId();
		given(auctionRepository.findById(auctionId)).willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> auctionService.getAuctionById(auctionId))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(AuctionErrorCode.NOT_FOUND_AUCTION.getMessage());
	}
}
