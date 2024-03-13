package dev.handsup.bidding.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.dto.BiddingMapper;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.BiddingResponse;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BiddingService {

	private final BiddingRepository biddingRepository;
	private final AuctionService auctionService;
	private boolean isFirstBidding;

	private void validateBiddingPrice(int biddingPrice, Auction auction) {
		isFirstBidding = false;
		Integer maxBiddingPrice = biddingRepository.findMaxBiddingPriceByAuctionId(auction.getId());

		if (maxBiddingPrice == null) {
			// 입찰 내역이 없는 경우, 최소 입찰가부터 입찰 가능
			isFirstBidding = true;
			if (biddingPrice < auction.getInitPrice()) {
				throw new ValidationException(BiddingErrorCode.BIDDING_PRICE_LESS_THAN_INIT_PRICE);
			}
		} else {
			// 최고 입찰가보다 1000원 이상일 때만 입찰 가능
			if (biddingPrice < (maxBiddingPrice + 1000)) {
				throw new ValidationException(BiddingErrorCode.BIDDING_PRICE_NOT_HIGH_ENOUGH);
			}
		}
	}

	@Transactional
	public BiddingResponse registerBidding(RegisterBiddingRequest request, Long auctionId, User bidder) {
		Auction auction = auctionService.getAuctionById(auctionId);
		validateBiddingPrice(request.biddingPrice(), auction);

		Bidding savedBidding = biddingRepository.save(
			BiddingMapper.toBidding(request, auction, bidder)
		);
		if (isFirstBidding) {
			savedBidding.updateTradingStatusPreparing(); // 첫 입찰일 경우 준비중 상태로 변경
		}
		auction.updateCurrentBiddingPrice(savedBidding.getBiddingPrice());
		return BiddingMapper.toBiddingResponse(savedBidding);
	}

	@Transactional(readOnly = true)
	public PageResponse<BiddingResponse> getBidsOfAuction(Long auctionId, Pageable pageable) {
		Slice<BiddingResponse> biddingResponsePage = biddingRepository
			.findByAuctionIdOrderByBiddingPriceDesc(auctionId, pageable)
			.map(BiddingMapper::toBiddingResponse);
		return CommonMapper.toPageResponse(biddingResponsePage);
	}

	@Transactional
	public BiddingResponse completeTrading(Long biddingId, User seller) {
		Bidding bidding = findBiddingById(biddingId);
		validateAuthorization(bidding, seller);
		bidding.updateTradingStatusComplete();
		bidding.getAuction().updateBuyer(bidding.getBidder());
		return BiddingMapper.toBiddingResponse(bidding);
	}

	@Transactional
	public BiddingResponse cancelTrading(Long biddingId, User seller) {
		Bidding bidding = findBiddingById(biddingId);
		validateAuthorization(bidding, seller);
		bidding.updateTradingStatusCanceled();
		biddingRepository.findFirstByTradingStatus(TradingStatus.WAITING) // 다음 입찰 준비중 상태로 변경
			.ifPresent(Bidding::updateTradingStatusPreparing);
		return BiddingMapper.toBiddingResponse(bidding);
	}

	private void validateAuthorization(Bidding bidding, User seller) {
		if (!Objects.equals(bidding.getAuction().getSeller().getId(), seller.getId())) {
			throw new ValidationException(BiddingErrorCode.NOT_AUTHORIZED_SELLER);
		}
	}

	private Bidding findBiddingById(Long biddingId) {
		return biddingRepository.findById(biddingId)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING));
	}
}
