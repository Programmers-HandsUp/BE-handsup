package dev.handsup.bidding.service;

import static dev.handsup.bidding.exception.BiddingErrorCode.*;

import org.springframework.stereotype.Service;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.BiddingMapper;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.RegisterBiddingResponse;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BiddingService {

	private final BiddingRepository biddingRepository;

	private void validateBiddingPrice(int biddingPrice, Auction auction) {
		Integer maxBiddingPrice = biddingRepository.findMaxBiddingPriceByAuctionId(auction.getId());

		if (maxBiddingPrice == null) {
			// 입찰 내역이 없는 경우, 최소 입찰가부터 입찰 가능
			if (biddingPrice < auction.getInitPrice()) {
				throw new ValidationException(BIDDING_PRICE_LESS_THAN_INIT_PRICE);
			}
		} else {
			// 최고 입찰가보다 1000원 이상일 때만 입찰 가능
			if (biddingPrice < (maxBiddingPrice + 1000)) {
				throw new ValidationException(BIDDING_PRICE_NOT_HIGH_ENOUGH);
			}
		}
	}

	public RegisterBiddingResponse registerBidding(RegisterBiddingRequest request) {
		validateBiddingPrice(request.biddingPrice(), request.auction());
		Bidding savedBidding = biddingRepository.save(BiddingMapper.toBidding(request));
		return BiddingMapper.toRegisterBiddingResponse(savedBidding);
	}
}
