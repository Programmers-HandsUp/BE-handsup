package dev.handsup.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
