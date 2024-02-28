package dev.handsup.auction.dto.response;

public record AuctionSimpleResponse(
	Long auctionId,
	String title,
	int initPrice,
	String imgUrl,
	int bookmarkCount,
	String dong,
	String createdDate

) {
	public static AuctionSimpleResponse of(
		Long auctionId,
		String title,
		int initPrice,
		String imgUrl,
		int bookmarkCount,
		String dong,
		String createDate
	) {
		return new AuctionSimpleResponse(
			auctionId,
			title,
			initPrice,
			imgUrl,
			bookmarkCount,
			dong,
			createDate
		);
	}
}
