package dev.handsup.auction.repository.auction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.user.domain.User;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

	@Query("select distinct b.auction from Bookmark b " +
		"where b.user = :user")
	Slice<Auction> findBookmarkAuction(@Param("user") User user, Pageable pageable);

	Slice<Auction> findBySeller_IdOrderByCreatedAtDesc(Long sellerId, Pageable pageable);

	Slice<Auction> findBySeller_IdAndStatusOrderByCreatedAtDesc(
		Long sellerId, AuctionStatus auctionStatus, Pageable pageable);
}
