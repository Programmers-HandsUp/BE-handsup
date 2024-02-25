package dev.handsup.search.dto;

import java.util.List;

public record PopularKeywordResponse(
	List<String> keywords
) {
	public static PopularKeywordResponse from(List<String> keywords){
		return new PopularKeywordResponse(keywords);
	}
}
