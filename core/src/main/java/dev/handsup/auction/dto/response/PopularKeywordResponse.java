package dev.handsup.auction.dto.response;

import java.util.List;

public record PopularKeywordResponse(
	List<String> keywords
) {
	public static PopularKeywordResponse from(List<String> keywords){
		return new PopularKeywordResponse(keywords);
	}
}
