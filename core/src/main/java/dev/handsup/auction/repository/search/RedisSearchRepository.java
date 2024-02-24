package dev.handsup.auction.repository.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSearchRepository{
	private final StringRedisTemplate redisTemplate;
	private static final String KEY = "RANKING";

	public List<String> getPopularKeywords(){
		Set<String> ranking = redisTemplate.opsForZSet()
			.reverseRange(KEY, 0, 9);
		if (ranking == null){
			return new ArrayList<>();
		}
		return new ArrayList<>(ranking);
	}

	public void increaseSearchCount(String keyword){
		int increaseAmount = 1;
		redisTemplate.opsForZSet()
			.incrementScore(KEY, keyword, increaseAmount);
	}

	//==테스트용 함수==//
	public void increaseSearchCount(String keyword, int increaseAmount){
		redisTemplate.opsForZSet()
			.incrementScore(KEY, keyword, increaseAmount);
	}

	public int getKeywordCount(String keyword){
		Double score = redisTemplate.opsForZSet().score(KEY, keyword);
		if (score==null) return 0; // 검색한 적 없을 때
		else
			return (int) Math.round(score);
	}

}
