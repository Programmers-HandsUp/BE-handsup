package dev.handsup.review.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record ReviewResponse(

	int evaluationScore,
	String content,
	Long auctionId,
	Long writerId
) {
	public static ReviewResponse of(
		int evaluationScore,
		String content,
		Long auctionId,
		Long writerId
	) {
		return ReviewResponse.builder()
			.evaluationScore(evaluationScore)
			.content(content)
			.auctionId(auctionId)
			.writerId(writerId)
			.build();
	}
}
