package dev.handsup.search.dto;

import static lombok.AccessLevel.*;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class SearchMapper {
	public static PopularKeywordResponse toPopularKeywordResponse(List<String> popularKeywords){
		return PopularKeywordResponse.from(popularKeywords);
	}

}
