package dev.handsup.bidding.service;

import static dev.handsup.bidding.exception.BiddingErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.BiddingMapper;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.RegisterBiddingResponse;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("[BiddingService 테스트]")
class BiddingServiceTest {

	private final Auction auction = AuctionFixture.auction();
	private final int initPrice = auction.getInitPrice(); // 최소 입찰가 10000원
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
		Long auctionId = auction.getId();
		given(auctionService.getAuctionEntity(auctionId)).willReturn(auction);

		given(biddingRepository.findMaxBiddingPriceByAuctionId(any(Long.class))).willReturn(null);
		RegisterBiddingRequest request = RegisterBiddingRequest.of(initPrice - 1000);

		// when & then
		assertThatThrownBy(() ->
			biddingService.registerBidding(request, auctionId, user)
		)
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(BIDDING_PRICE_LESS_THAN_INIT_PRICE.getMessage());
	}

	@Test
	@DisplayName("[입찰가가 최고 입찰가보다 1000원 이상 높지 않으면 예외를 발생시킨다]")
	void validateBiddingPrice_NotHighEnough_ThrowsException() {
		// given
		Long auctionId = auction.getId();
		given(auctionService.getAuctionEntity(auctionId)).willReturn(auction);

		int maxBiddingPrice = 15000;
		given(biddingRepository.findMaxBiddingPriceByAuctionId(any(Long.class))).willReturn(maxBiddingPrice);
		RegisterBiddingRequest request = RegisterBiddingRequest.of(maxBiddingPrice + 500);

		// when & then
		assertThatThrownBy(() ->
			biddingService.registerBidding(request, auctionId, user)
		)
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(BIDDING_PRICE_NOT_HIGH_ENOUGH.getMessage());
	}

	@Test
	@DisplayName("[입찰 등록이 성공적으로 이루어진다]")
	void registerBidding_Success() {
		// given
		int maxBiddingPrice = 19000;
		given(biddingRepository.findMaxBiddingPriceByAuctionId(any(Long.class))).willReturn(maxBiddingPrice);

		Long auctionId = auction.getId();
		given(auctionService.getAuctionEntity(auctionId)).willReturn(auction);

		RegisterBiddingRequest request = RegisterBiddingRequest.of(maxBiddingPrice + 1000);
		Bidding bidding = BiddingMapper.toBidding(request, auction, user);
		given(biddingRepository.save(any(Bidding.class))).willReturn(bidding);

		// when
		RegisterBiddingResponse response = biddingService.registerBidding(request, auctionId, user);

		// then
		assertThat(response).isNotNull();
		verify(biddingRepository).save(any(Bidding.class));
	}
}
