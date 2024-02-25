package dev.handsup.search.dto;

import java.util.List;

public record PopularKeywordsResponse(
	List<PopularKeywordResponse> list
) {
	public static PopularKeywordsResponse from(List<PopularKeywordResponse> responses){
		return new PopularKeywordsResponse(responses);
	}
}
