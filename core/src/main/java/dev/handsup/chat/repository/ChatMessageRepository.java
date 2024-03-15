package dev.handsup.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	Slice<ChatMessage> findByChatRoomOrderByCreatedAt(ChatRoom chatRoom, Pageable pageable);
}
