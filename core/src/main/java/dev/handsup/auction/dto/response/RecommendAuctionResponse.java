package dev.handsup.auction.dto.response;

public record RecommendAuctionResponse(
	String title,
	String dong,
	int currentBiddingPrice,
	String imgUrl,
	int bookmarkCount,
	int biddingCount,
	String createdAt
) {
	public static RecommendAuctionResponse of(
		String title,
		String dong,
		int currentBiddingPrice,
		String imgUrl,
		int bookmarkCount,
		int biddingCount,
		String createdAt
	) {
		return new RecommendAuctionResponse(
			title, dong, currentBiddingPrice, imgUrl, bookmarkCount, biddingCount, createdAt);
	}
}
