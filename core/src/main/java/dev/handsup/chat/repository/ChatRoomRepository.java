package dev.handsup.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.user.domain.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	@Query("SELECT cr FROM ChatRoom cr WHERE cr.seller = :user OR cr.buyer = :user")
	Slice<ChatRoom> findChatRoomsByUser(User user, Pageable pageable);

	Boolean existsByAuctionIdAndBuyer(Long auctionId, User buyer);
	Boolean existsByAuctionId(Long auctionId);
}
