package dev.handsup.common.config;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@TestConfiguration
public class RedisTestConfig {

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		return mock(StringRedisTemplate.class);
	}
}
