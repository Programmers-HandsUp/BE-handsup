package dev.handsup.auction.dto.response;

public record AuctionSimpleResponse(

	Long auctionId,
	String title,
	int currentBiddingPrice,
	String imageUrl,
	int bookmarkCount,
	String dong,
	String createdAt,
	String status
) {
	public static AuctionSimpleResponse of(
		Long auctionId,
		String title,
		int currentBiddingPrice,
		String imageUrl,
		int bookmarkCount,
		String dong,
		String createdAt,
		String status
	) {
		return new AuctionSimpleResponse(
			auctionId,
			title,
			currentBiddingPrice,
			imageUrl,
			bookmarkCount,
			dong,
			createdAt,
			status
		);
	}
}
