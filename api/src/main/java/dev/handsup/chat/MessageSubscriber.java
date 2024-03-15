package dev.handsup.chat;

import static java.util.Objects.*;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.handsup.chat.dto.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSubscriber implements MessageListener {

	private final ObjectMapper objectMapper;
	private final RedisTemplate<String, Object> redisTemplate;
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String publishMessage = redisTemplate.getStringSerializer()
			.deserialize(message.getBody());
		String channel = redisTemplate.getStringSerializer().deserialize(message.getChannel());
		try {
			ChatMessageResponse response = objectMapper.readValue(publishMessage, ChatMessageResponse.class);
			messagingTemplate.convertAndSend(requireNonNull(channel), response);
		} catch (JacksonException exception) {
			throw new RuntimeException("메시지 JSON 매핑에 실패했습니다.");
		}
	}
}
