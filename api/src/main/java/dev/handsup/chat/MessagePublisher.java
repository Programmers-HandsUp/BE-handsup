package dev.handsup.chat;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessagePublisher {
	private final RedisTemplate<String, Object> redisTemplate;

	public void publish(String channel, Object message) {
		redisTemplate.convertAndSend(channel, message);
	}
}
