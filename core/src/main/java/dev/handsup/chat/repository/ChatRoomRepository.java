package dev.handsup.chat.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.user.domain.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("SELECT cr FROM ChatRoom cr "
		+ "WHERE cr.seller = :user OR cr.bidder = :user ORDER BY cr.createdAt DESC")
	Slice<ChatRoom> findChatRoomsByUser(@Param("user") User user, Pageable pageable);

	Optional<ChatRoom> findChatRoomByCurrentBiddingId(Long currentBiddingId);

	Optional<ChatRoom> findByAuctionIdAndBidder(Long auctionId, User bidder);
}
