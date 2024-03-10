package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatMessageFixture {
	public static ChatMessage chatMessage(ChatRoom chatRoom, User sender) {
		ChatMessage chatMessage = ChatMessage.of(
			chatRoom,
			sender.getId(),
			"와"
		);
		ReflectionTestUtils.setField(chatMessage, "createdAt", LocalDateTime.now());
		return chatMessage;
	}
}
