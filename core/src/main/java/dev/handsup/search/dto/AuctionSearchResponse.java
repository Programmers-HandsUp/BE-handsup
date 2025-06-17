package dev.handsup.search.dto;

public record AuctionSearchResponse(

	Long auctionId,
	String title,
	int currentBiddingPrice,
	String imageUrl,
	int bookmarkCount,
	String dong,
	String createdAt,
	boolean isProgress
) {
	public static AuctionSearchResponse of(
		Long auctionId,
		String title,
		int currentBiddingPrice,
		String imageUrl,
		int bookmarkCount,
		String dong,
		String createdAt,
		boolean isProgress
	) {
		return new AuctionSearchResponse(
			auctionId,
			title,
			currentBiddingPrice,
			imageUrl,
			bookmarkCount,
			dong,
			createdAt,
			isProgress
		);
	}
}
