package dev.handsup.review.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record ReviewSimpleResponse(

	Long reviewId,
	int evaluationScore,
	String content,
	Long auctionId,
	Long writerId,
	String writerNickName,
	String reviewCreatedAt
) {
	public static ReviewSimpleResponse of(
		Long reviewId,
		int evaluationScore,
		String content,
		Long auctionId,
		Long writerId,
		String writerNickName,
		String reviewCreatedAt
	) {
		return ReviewSimpleResponse.builder()
			.reviewId(reviewId)
			.evaluationScore(evaluationScore)
			.content(content)
			.auctionId(auctionId)
			.writerId(writerId)
			.writerNickName(writerNickName)
			.reviewCreatedAt(reviewCreatedAt)
			.build();
	}
}
