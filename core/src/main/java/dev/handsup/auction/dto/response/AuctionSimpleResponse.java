package dev.handsup.auction.dto.response;

public record AuctionSimpleResponse(
	Long auctionId,
	String title,
	int currentBiddingPrice,
	String imageUrl,
	int bookmarkCount,
	String dong,
	String createdAt

) {
	public static AuctionSimpleResponse of(
		Long auctionId,
		String title,
		int currentBiddingPrice,
		String imageUrl,
		int bookmarkCount,
		String dong,
		String createdAt
	) {
		return new AuctionSimpleResponse(
			auctionId,
			title,
			currentBiddingPrice,
			imageUrl,
			bookmarkCount,
			dong,
			createdAt
		);
	}
}
