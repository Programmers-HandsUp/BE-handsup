package dev.handsup.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	Slice<ChatMessage> findByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom, Pageable pageable);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(
		"update ChatMessage m set m.isRead = true "
			+ "where m.chatRoom = :chatRoom "
			+ "and m.senderId != :senderId"
	)
	void readReceivedMessages(
		@Param("chatRoom") ChatRoom chatRoom,
		@Param("senderId") Long senderId
	);
}
