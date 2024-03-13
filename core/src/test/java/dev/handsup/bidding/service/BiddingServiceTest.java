package dev.handsup.bidding.service;

import static dev.handsup.bidding.exception.BiddingErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.BiddingResponse;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("[BiddingService 테스트]")
class BiddingServiceTest {

	private final Auction auction = AuctionFixture.auction();    // 최소 입찰가 10000원
	private final User user = UserFixture.user();
	@Mock
	private BiddingRepository biddingRepository;
	@Mock
	private AuctionService auctionService;
	@InjectMocks
	private BiddingService biddingService;

	@Test
	@DisplayName("[입찰가가 최소 입찰가보다 낮으면 예외를 발생시킨다]")
	void validateBiddingPrice_LessThanInitPrice_ThrowsException() {
		// given
		given(biddingRepository.findMaxBiddingPriceByAuctionId(any(Long.class))).willReturn(null);

		RegisterBiddingRequest request = RegisterBiddingRequest.from(9000);
		Long auctionId = auction.getId();
		given(auctionService.getAuctionById(auctionId)).willReturn(auction);

		// when & then
		assertThatThrownBy(() -> biddingService.registerBidding(request, auctionId, user))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(BIDDING_PRICE_LESS_THAN_INIT_PRICE.getMessage());
	}

	@Test
	@DisplayName("[입찰가가 최고 입찰가보다 1000원 이상 높지 않으면 예외를 발생시킨다]")
	void validateBiddingPrice_NotHighEnough_ThrowsException() {
		// given
		Integer maxBiddingPrice = 15000;
		given(biddingRepository.findMaxBiddingPriceByAuctionId(any(Long.class))).willReturn(maxBiddingPrice);

		RegisterBiddingRequest request = RegisterBiddingRequest.from(15500);
		Long auctionId = auction.getId();
		given(auctionService.getAuctionById(auctionId)).willReturn(auction);

		// when & then
		assertThatThrownBy(() -> biddingService.registerBidding(request, auctionId, user))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(BIDDING_PRICE_NOT_HIGH_ENOUGH.getMessage());
	}

	@Test
	@DisplayName("[입찰 등록이 성공적으로 이루어진다]")
	void registerBidding_Success() {
		// given
		RegisterBiddingRequest request = RegisterBiddingRequest.from(20000);

		given(auctionService.getAuctionById(auction.getId())).willReturn(auction);
		Bidding bidding = Bidding.of(
			request.biddingPrice(),
			auction,
			user
		);
		ReflectionTestUtils.setField(bidding, "createdAt", LocalDateTime.now());
		given(biddingRepository.save(any(Bidding.class))).willReturn(bidding);
		given(biddingRepository.findMaxBiddingPriceByAuctionId(any(Long.class))).willReturn(19000);

		// when
		BiddingResponse response = biddingService.registerBidding(request, auction.getId(), user);

		// then
		assertThat(response).isNotNull();
		verify(biddingRepository).save(any(Bidding.class));
	}

	@Test
	@DisplayName("[경매 ID에 대한 입찰 목록을 페이지 형태로 조회한다]")
	void getBidsOfAuction_Success() {
		// given
		Long auctionId = 1L;
		Pageable pageRequest = PageRequest.of(0, 10);

		Bidding bidding1 = Bidding.of(40000, auction, user);
		Bidding bidding2 = Bidding.of(30000, auction, user);
		Bidding bidding3 = Bidding.of(20000, auction, user);
		List<Bidding> mockBiddingList = List.of(
			bidding1,
			bidding2,
			bidding3
		);
		Slice<Bidding> mockSlice = new SliceImpl<>(mockBiddingList, pageRequest, true);
		ReflectionTestUtils.setField(bidding1, "createdAt", LocalDateTime.now());
		ReflectionTestUtils.setField(bidding2, "createdAt", LocalDateTime.now());
		ReflectionTestUtils.setField(bidding3, "createdAt", LocalDateTime.now());

		given(biddingRepository.findByAuctionIdOrderByBiddingPriceDesc(auctionId, pageRequest))
			.willReturn(mockSlice);

		// when
		PageResponse<BiddingResponse> response =
			biddingService.getBidsOfAuction(auctionId, pageRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.content()).hasSize(3);
		assertThat(response.content().get(0).biddingPrice()).isEqualTo(40000);
		assertThat(response.content().get(1).biddingPrice()).isEqualTo(30000);
		assertThat(response.content().get(2).biddingPrice()).isEqualTo(20000);

		verify(biddingRepository).findByAuctionIdOrderByBiddingPriceDesc(auctionId, pageRequest);
	}

	@DisplayName("[판매자는 진행 중인 거래를 완료 상태로 변경할 수 있다.]")
	@Test
	void completeTrading() {
		//given
		User bidder = UserFixture.user("bidder@gmail.com");
		Bidding bidding = BiddingFixture.bidding(auction, bidder, TradingStatus.PROGRESSING);
		ReflectionTestUtils.setField(bidding, "createdAt", LocalDateTime.now());

		given(biddingRepository.findById(1L)).willReturn(Optional.of(bidding));

		//when
		BiddingResponse response = biddingService.completeTrading(bidding.getId(), user);

		//then
		assertThat(response.tradingStatus()).isEqualTo(TradingStatus.COMPLETED.getLabel());
		assertThat(bidding.getAuction().getBuyer()).isEqualTo(bidder);
	}

	@DisplayName("[판매자는 진행 중인 거래를 취소 상태로 변경할 수 있다.]")
	@Test
	void cancelTrading() {
		//given
		User bidder = UserFixture.user("bidder@gmail.com");
		Bidding bidding1 = BiddingFixture.bidding(auction, bidder, TradingStatus.PROGRESSING, 40000);
		ReflectionTestUtils.setField(bidding1, "createdAt", LocalDateTime.now());

		Bidding bidding2 = BiddingFixture.bidding(auction, bidder, TradingStatus.WAITING,
			bidding1.getBiddingPrice() + 1000);
		given(biddingRepository.findById(1L)).willReturn(Optional.of(bidding1));
		given(biddingRepository.findFirstByTradingStatus(TradingStatus.WAITING)).willReturn(Optional.of(bidding2));

		//when
		BiddingResponse response = biddingService.cancelTrading(bidding1.getId(), user);

		//then
		assertThat(response.tradingStatus()).isEqualTo(TradingStatus.CANCELED.getLabel());
		assertThat(bidding2.getTradingStatus()).isEqualTo(TradingStatus.PREPARING);
	}
}
