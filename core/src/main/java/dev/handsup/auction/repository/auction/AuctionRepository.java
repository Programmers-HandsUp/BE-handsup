package dev.handsup.auction.repository.auction;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.user.domain.User;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

	@Query("select distinct b.auction from Bookmark b " +
		"where b.user = :user")
	Slice<Auction> findBookmarkAuction(@Param("user") User user, Pageable pageable);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Auction a set a.status = :newStatus "
		+ "where a.status = :currentStatus and a.endDate < :todayDate")
	void updateAuctionStatus(
		@Param("currentStatus") AuctionStatus currentStatus,
		@Param("newStatus") AuctionStatus newStatus,
		@Param("todayDate") LocalDate todayDate
	);
}