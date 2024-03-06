package dev.handsup.chat.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.user.domain.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("SELECT cr FROM ChatRoom cr WHERE cr.seller = :user OR cr.bidder = :user")
	Slice<ChatRoom> findChatRoomsByUser(User user, Pageable pageable);

	Optional<ChatRoom> findChatRoomByAuctionIdAndBidder(Long auctionId, User bidder);

	Boolean existsByAuctionIdAndBidder(Long auctionId, User bidder);
}
