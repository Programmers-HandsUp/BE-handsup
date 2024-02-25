package dev.handsup.search.dto;

public record PopularKeywordResponse(
	String keyword,
	int count
) {
	public static PopularKeywordResponse of(String keyword, int count){
		return new PopularKeywordResponse(keyword, count);
	}
}
