package dev.handsup.bidding.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.user.domain.User;

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
	Integer findMaxBiddingPriceByAuctionId(@Param("auctionId") Long auctionId);

	Slice<Bidding> findByAuctionIdOrderByBiddingPriceDesc(Long auctionId, Pageable pageable);

	Optional<Bidding> findByAuctionAndBidder(Auction auction, User bidder);

	Slice<Bidding> findByBidderOrderByAuction_CreatedAtDesc(User bidder, Pageable pageable);

	Slice<Bidding> findByBidderAndAuction_StatusOrderByAuction_CreatedAtDesc(
		User bidder, AuctionStatus auctionStatus, Pageable pageable);

	Slice<Bidding> findByAuction_Seller_IdOrderByAuction_CreatedAtDesc(Long sellerId, Pageable pageable);

	Slice<Bidding> findByAuction_Seller_IdAndAuction_StatusOrderByAuction_CreatedAtDesc(
		Long sellerId, AuctionStatus auctionStatus, Pageable pageable);
}
