package dev.handsup.auction.repository.search;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.handsup.support.TestContainerSupport;

@SpringBootTest
class RedisSearchRepositoryTest extends TestContainerSupport {

	@Autowired
	RedisSearchRepository repository;

	@DisplayName("[검색어 score(검색 수)를 1만큼 증가시킬 수 있다.]")
	@Test
	void increaseSearchCount() {
		//given
		final String KEYWORD = "검색어";
		int previousCount = repository.getKeywordCount(KEYWORD);

		//when
		repository.increaseSearchCount(KEYWORD);
		int count = repository.getKeywordCount(KEYWORD);

		//then
		assertThat(count).isEqualTo(previousCount + 1);
	}

	@DisplayName("[검색어를 인기순으로 조회할 수 있다.]")
	@Test
	void getPopularKeywords() {
		//given
		final String KEYWORD1 = "검색어1", KEYWORD2 = "검색어2", KEYWORD3 = "검색어3";
		repository.increaseSearchCount(KEYWORD1, 1);
		repository.increaseSearchCount(KEYWORD2, 3);
		repository.increaseSearchCount(KEYWORD3, 5);

		//when
		List<String> popularKeywords = repository.getPopularKeywords();

		//then
		assertThat(repository.getKeywordCount(KEYWORD1)).isEqualTo(1);
		assertThat(repository.getKeywordCount(KEYWORD2)).isEqualTo(3);
		assertThat(repository.getKeywordCount(KEYWORD3)).isEqualTo(5);
		assertThat(popularKeywords).containsExactly(KEYWORD3, KEYWORD2, KEYWORD1);
	}
}