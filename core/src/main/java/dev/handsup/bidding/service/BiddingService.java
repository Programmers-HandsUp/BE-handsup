package dev.handsup.bidding.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
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
import dev.handsup.common.redisson.DistributeLock;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.service.FCMService;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BiddingService {

	private final BiddingRepository biddingRepository;
	private final BiddingQueryRepository biddingQueryRepository;
	private final AuctionRepository auctionRepository;
	private final FCMService fcmService;
	private final ObjectProvider<BiddingService> biddingServiceProvider;


	@Transactional
	public BiddingResponse registerBidding(RegisterBiddingRequest request, Long auctionId, User bidder) {
		Auction auction = getAuctionById(auctionId);
		validateNotSelfBidding(bidder, auction); // 입찰 불가한 판매자 아닌지 검증

		return biddingServiceProvider.getObject()
			.registerBiddingWithLock(auction, request.biddingPrice(), bidder);
	}

	@DistributeLock(key = "'auction_' + #auction.getId()") // auctionId 값을 추출하여 락 키로 사용
	public BiddingResponse registerBiddingWithLock(Auction auction, int biddingPrice, User bidder) {
		validateBiddingPrice(biddingPrice, auction); // 경매 입찰 최고가보다 입찰가 높은지 확인
		auction.updateCurrentBiddingPrice(biddingPrice); // 경매 입찰 최고가 갱신
		auction.increaseBiddingCount(); // 경매 입찰 수 + 1
		Bidding bidding = BiddingMapper.toBidding(biddingPrice, auction, bidder);
		return BiddingMapper.toBiddingResponse(biddingRepository.save(bidding));
	}

	@Transactional(readOnly = true)
	public PageResponse<BiddingResponse> getBidsOfAuction(Long auctionId, Pageable pageable) {
		Slice<BiddingResponse> biddingResponsePage = biddingRepository
			.findByAuctionIdOrderByBiddingPriceDesc(auctionId, pageable)
			.map(BiddingMapper::toBiddingResponse);
		return CommonMapper.toPageResponse(biddingResponsePage);
	}

	@Transactional
	public BiddingResponse completeTrading(Long biddingId, User user) {
		Bidding bidding = findBiddingById(biddingId);
		bidding.getAuction().validateIfSeller(user);

		bidding.updateTradingStatusComplete();
		bidding.getAuction().updateAuctionStatusCompleted();
		bidding.getAuction().updateBuyer(bidding.getBidder());
		sendMessage(user, bidding, NotificationType.COMPLETED_PURCHASE_TRADING);
		return BiddingMapper.toBiddingResponse(bidding);
	}

	@Transactional
	public BiddingResponse cancelTrading(Long biddingId, User user) {
		Bidding bidding = findBiddingById(biddingId);
		bidding.getAuction().validateIfSeller(user);
		bidding.updateTradingStatusCanceled();

		Bidding nextBidding = biddingQueryRepository.findWaitingBiddingLatest(bidding.getAuction())
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_NEXT_BIDDING));
		nextBidding.updateTradingStatusPreparing();    // 다음 입찰 준비중 상태로 변경

		// 현재 입찰자 거래 취소 알림
		sendMessage(user, bidding, NotificationType.CANCELED_PURCHASE_TRADING);

		// 다음 입찰자 낙찰 알림
		sendMessage(user, nextBidding, NotificationType.PURCHASE_WINNING);

		return BiddingMapper.toBiddingResponse(bidding);
	}

	public void validateBiddingPrice(int biddingPrice, Auction auction) {
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

	private void validateNotSelfBidding(User bidder, Auction auction) {
		if (auction.getSeller().equals(bidder)) {
			throw new ValidationException(BiddingErrorCode.NOT_ALLOW_SELF_BIDDING);
		}
	}

	private Bidding findBiddingById(Long biddingId) {
		return biddingRepository.findById(biddingId)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING));
	}

	private Auction getAuctionById(Long auctionId){
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
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
