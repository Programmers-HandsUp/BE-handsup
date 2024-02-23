package dev.handsup.auction.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record FindUserBookmarkResponse(
	Long auctionId,
	String title,
	String auctionStatus,
	LocalDateTime createdAt,
	String imgUrl
) {
	public static FindUserBookmarkResponse from(
		Long auctionId,
		String title,
		String auctionStatus,
		LocalDateTime createdAt,
		String imgUrl
	){
		return new FindUserBookmarkResponse(auctionId, title, auctionStatus, createdAt, imgUrl);
	}
}
