package dev.handsup.bookmark.dto;

import lombok.Builder;

@Builder
public record FindUserBookmarkResponse(
	Long auctionId,
	String title,
	String auctionStatus,
	String createdDate,
	String imgUrl
) {
	public static FindUserBookmarkResponse from(
		Long auctionId,
		String title,
		String auctionStatus,
		String createdDate,
		String imgUrl
	) {
		return new FindUserBookmarkResponse(auctionId, title, auctionStatus, createdDate, imgUrl);
	}
}
