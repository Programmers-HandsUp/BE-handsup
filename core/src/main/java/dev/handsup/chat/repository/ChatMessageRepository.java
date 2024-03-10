package dev.handsup.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
