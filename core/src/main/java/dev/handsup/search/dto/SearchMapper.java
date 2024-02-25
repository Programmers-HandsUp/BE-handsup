package dev.handsup.search.dto;

import static lombok.AccessLevel.*;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class SearchMapper {
	public static PopularKeywordsResponse toPopularKeywordsResponse(List<PopularKeywordResponse> responses) {
		return PopularKeywordsResponse.from(responses);
	}

}
