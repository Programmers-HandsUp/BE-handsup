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

	@Query("SELECT MAX(b.biddingPrice) FROM Bidding b WHERE b.auction.id = :auctionId")
	Integer findMaxBiddingPriceByAuctionId(Long auctionId);

	Slice<Bidding> findByAuctionIdOrderByBiddingPriceDesc(Long auctionId, Pageable pageable);

	Optional<Bidding> findByAuctionAndBidder(Auction auction, User bidder);

	@Query("SELECT b FROM Bidding b "
		+ "WHERE b.bidder = :bidder "
		+ "ORDER BY b.auction.createdAt DESC")
	Slice<Bidding> findByBidderOrderByAuction_CreatedAtDesc(User bidder, Pageable pageable);

	@Query("SELECT b FROM Bidding b "
		+ "WHERE b.bidder = :bidder "
		+ "AND b.auction.status = :auctionStatus "
		+ "ORDER BY b.auction.createdAt DESC")
	Slice<Bidding> findByBidderAndAuction_StatusOrderByAuction_CreatedAtDesc(
		User bidder, AuctionStatus auctionStatus, Pageable pageable);

	@Query("SELECT b FROM Bidding b "
		+ "WHERE b.auction.seller.id = :sellerId "
		+ "ORDER BY b.auction.createdAt DESC")
	Slice<Bidding> findByAuction_Seller_IdOrderByAuction_CreatedAtDesc(
		Long sellerId, Pageable pageable);

	@Query("SELECT b FROM Bidding b WHERE b.auction.seller.id = :sellerId "
		+ "AND b.auction.status = :auctionStatus "
		+ "ORDER BY b.auction.createdAt DESC")
	Slice<Bidding> findByAuction_Seller_IdAndAuction_StatusOrderByAuction_CreatedAtDesc(
		Long sellerId, AuctionStatus auctionStatus, Pageable pageable);
}
