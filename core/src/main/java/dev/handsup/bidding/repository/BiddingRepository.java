package dev.handsup.bidding.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.user.domain.User;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {

	@Query("SELECT MAX(b.biddingPrice) FROM Bidding b WHERE b.auction.id = :auctionId")
	Integer findMaxBiddingPriceByAuctionId(Long auctionId);

	Slice<Bidding> findByAuctionIdOrderByBiddingPriceDesc(Long auctionId, Pageable pageable);

	Optional<Bidding> findFirstByTradingStatus(TradingStatus tradingStatus);

	Optional<Bidding> findByAuctionAndBidder(Auction auction, User bidder);
}
