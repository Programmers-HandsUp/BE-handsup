package dev.handsup.bidding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.handsup.bidding.domain.Bidding;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {

	@Query("SELECT MAX(b.biddingPrice) FROM Bidding b WHERE b.auction.id = :auctionId")
	Integer findMaxBiddingPriceByAuctionId(Long auctionId);
}
