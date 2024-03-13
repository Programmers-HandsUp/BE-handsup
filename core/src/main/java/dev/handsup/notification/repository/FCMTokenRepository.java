package dev.handsup.notification.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import dev.handsup.auth.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FCMTokenRepository {

	private final StringRedisTemplate tokenRedisTemplate;

	public void saveFcmToken(LoginRequest loginRequest) {
		tokenRedisTemplate.opsForValue()
			.set(loginRequest.email(), loginRequest.fcmToken());
	}

	public String getFcmToken(String email) {
		return tokenRedisTemplate.opsForValue().get(email);
	}

	public void deleteFcmToken(String email) {
		tokenRedisTemplate.delete(email);
	}

	public boolean hasKey(String email) {
		return Boolean.TRUE.equals(tokenRedisTemplate.hasKey(email));
	}
}
