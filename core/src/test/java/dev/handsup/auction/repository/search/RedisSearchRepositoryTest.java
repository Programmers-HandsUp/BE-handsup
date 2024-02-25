package dev.handsup.auction.repository.search;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.handsup.search.dto.PopularKeywordResponse;
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
		List<PopularKeywordResponse> popularKeywords = repository.getPopularKeywords();

		//then
		assertAll(
			() -> assertThat(popularKeywords.get(0).count()).isEqualTo(5),
			() ->assertThat(popularKeywords.get(1).count()).isEqualTo(3),
			() ->assertThat(popularKeywords.get(2).count()).isEqualTo(1),
			() ->assertThat(popularKeywords.get(0).keyword()).isEqualTo(KEYWORD3),
			() ->assertThat(popularKeywords.get(1).keyword()).isEqualTo(KEYWORD2),
			() ->assertThat(popularKeywords.get(2).keyword()).isEqualTo(KEYWORD1)
		);

	}
}