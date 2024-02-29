package dev.handsup.auction.dto.response;

public record AuctionSimpleResponse(
	Long auctionId,
	String title,
	int initPrice,
	String imageUrl,
	int bookmarkCount,
	String dong,
	String createdAt

) {
	public static AuctionSimpleResponse of(
		Long auctionId,
		String title,
		int initPrice,
		String imageUrl,
		int bookmarkCount,
		String dong,
		String createdAt
	) {
		return new AuctionSimpleResponse(
			auctionId,
			title,
			initPrice,
			imageUrl,
			bookmarkCount,
			dong,
			createdAt
		);
	}
}
