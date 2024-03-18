package dev.handsup.bidding.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.handsup.bidding.domain.Bidding;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {

	@Query("SELECT b FROM Bidding b "
		+ "JOIN FETCH b.auction "
		+ "JOIN FETCH b.bidder "
		+ "WHERE b.id = :id")
	Optional<Bidding> findBiddingWithAuctionAndBidder(@Param("id") Long id);

	@Query("SELECT b FROM Bidding b "
		+ "JOIN FETCH b.auction "
		+ "WHERE b.id = :id")
	Optional<Bidding> findBiddingWithAuction(@Param("id") Long id);

	@Query("SELECT MAX(b.biddingPrice) FROM Bidding b WHERE b.auction.id = :auctionId")
	Integer findMaxBiddingPriceByAuctionId(Long auctionId);

	Slice<Bidding> findByAuctionIdOrderByBiddingPriceDesc(Long auctionId, Pageable pageable);
}
