package dev.handsup.bookmark.dto;

import lombok.Builder;

@Builder
public record FindUserBookmarkResponse(
	Long auctionId,
	String title,
	String auctionStatus,
	String imageUrl,
	String createdDate

) {
	public static FindUserBookmarkResponse of(
		Long auctionId,
		String title,
		String auctionStatus,
		String imageUrl,
		String createdDate
	) {
		return new FindUserBookmarkResponse(auctionId, title, auctionStatus, imageUrl, createdDate);
	}
}
