package dev.handsup.auction.repository.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import dev.handsup.search.domain.KeywordType;
import dev.handsup.search.dto.PopularKeywordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSearchRepository {
	private final StringRedisTemplate redisTemplate;

	public List<PopularKeywordResponse> getPopularKeywords(int number) {
		// 인기순 검색어, 검색 수 조회
		Set<ZSetOperations.TypedTuple<String>> typedTuples = getKeywordsWithScore(KeywordType.POPULAR.getKey(), number);

		// set(typedTuple) -> list(response) 로 변환
		List<PopularKeywordResponse> popularKeywords = new ArrayList<>();

		if (typedTuples != null) {
			for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
				String keyword = typedTuple.getValue();
				int count = Objects.requireNonNull(typedTuple.getScore()).intValue();

				popularKeywords.add(PopularKeywordResponse.of(keyword, count));
			}
		}

		return popularKeywords;
	}

	public Set<ZSetOperations.TypedTuple<String>> getKeywordsWithScore(String key, int number) {
		return redisTemplate.opsForZSet()
			.reverseRangeWithScores(key, 0, number);
	}

	public void increaseSearchCount(String keyword) {
		int increaseAmount = 1;
		redisTemplate.opsForZSet()
			.incrementScore(KeywordType.POPULAR.getKey(), keyword, increaseAmount);
	}

	//==테스트용 함수==//
	public void increaseSearchCount(String keyword, int increaseAmount) {
		redisTemplate.opsForZSet()
			.incrementScore(KeywordType.POPULAR.getKey(), keyword, increaseAmount);
	}

	public int getKeywordCount(String keyword) {
		Double score = redisTemplate.opsForZSet().score(KeywordType.POPULAR.getKey(), keyword);
		if (score == null)
			return 0; // 검색한 적 없을 때
		else
			return (int)Math.round(score);
	}

}
