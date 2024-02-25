package dev.handsup.auction.repository.auction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.handsup.auction.domain.Auction;
import dev.handsup.user.domain.User;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
	@Query("select distinct b.auction from Bookmark b " +
		"where b.user = :user")
	Slice<Auction> findBookmarkAuction(@Param("user") User user, Pageable pageable);
}