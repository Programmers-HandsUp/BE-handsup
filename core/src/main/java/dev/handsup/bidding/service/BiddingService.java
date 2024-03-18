package dev.handsup.bidding.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.BiddingMapper;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.BiddingResponse;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingQueryRepository;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.service.FCMService;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BiddingService {

	private final BiddingRepository biddingRepository;
	private final BiddingQueryRepository biddingQueryRepository;
	private final AuctionService auctionService;
	private final FCMService fcmService;

	private void validateBiddingPrice(int biddingPrice, Auction auction) {
		Integer maxBiddingPrice = biddingRepository.findMaxBiddingPriceByAuctionId(auction.getId());

		if (maxBiddingPrice == null) {
			// 입찰 내역이 없는 경우, 최소 입찰가부터 입찰 가능
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

		if (auction.getSeller().equals(bidder)) {
			throw new ValidationException(BiddingErrorCode.NOT_ALLOW_SELF_BIDDING);
		}

		validateBiddingPrice(request.biddingPrice(), auction);

		Bidding savedBidding = biddingRepository.save(
			BiddingMapper.toBidding(request, auction, bidder)
		);

		auction.updateCurrentBiddingPrice(savedBidding.getBiddingPrice());
		auction.increaseBiddingCount();

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

		// 거래 완료 알림 추가
		bidding.getAuction().changeAuctionStatusCompleted();

		sendMessage(seller, bidding, NotificationType.COMPLETED_PURCHASE_TRADING);

		return BiddingMapper.toBiddingResponse(bidding);
	}

	@Transactional
	public BiddingResponse cancelTrading(Long biddingId, User seller) {
		Bidding bidding = findBiddingById(biddingId);
		validateAuthorization(bidding, seller);
		bidding.updateTradingStatusCanceled();

		Bidding nextBidding = biddingQueryRepository.findWaitingBiddingLatest(bidding.getAuction())
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_NEXT_BIDDING));
		nextBidding.updateTradingStatusPreparing();    // 다음 입찰 준비중 상태로 변경

		// 현재 입찰자 거래 취소 알림
		sendMessage(seller, bidding, NotificationType.CANCELED_PURCHASE_TRADING);

		// 다음 입찰자 낙찰 알림
		sendMessage(seller, nextBidding, NotificationType.PURCHASE_WINNING);

		return BiddingMapper.toBiddingResponse(bidding);
	}

	private void validateAuthorization(Bidding bidding, User seller) {
		if (!Objects.equals(bidding.getAuction().getSeller().getId(), seller.getId())) {
			throw new ValidationException(BiddingErrorCode.NOT_AUTHORIZED_SELLER);
		}
	}

	public Bidding findBiddingById(Long biddingId) {
		return biddingRepository.findById(biddingId)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING));
	}

	private void sendMessage(User seller, Bidding bidding, NotificationType completedPurchaseTrading) {
		fcmService.sendMessage(
			seller.getEmail(),
			seller.getNickname(),
			bidding.getBidder().getEmail(),
			completedPurchaseTrading,
			bidding.getAuction()
		);
	}

}
