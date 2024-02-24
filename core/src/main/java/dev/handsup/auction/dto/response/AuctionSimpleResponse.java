package dev.handsup.auction.dto.response;

public record AuctionSimpleResponse(
	Long auctionId,
	String title,
	int initPrice,
	int bookmarkCount,
	String dong,
	String createdDate,
	String profileImgUrl
) {
	public static AuctionSimpleResponse of(
		Long auctionId,
		String title,
		int initPrice,
		int bookmarkCount,
		String dong,
		String createDate,
		String profileImgUrl
	) {
		return new AuctionSimpleResponse(
			auctionId,
			title,
			initPrice,
			bookmarkCount,
			dong,
			createDate,
			profileImgUrl
		);
	}
}
