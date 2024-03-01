package dev.handsup.auction.dto.response;

public record RecommendAuctionResponse(
	Long auctionId,
	String title,
	String dong,
	int currentBiddingPrice,
	String imgUrl,
	int bookmarkCount,
	int biddingCount,
	String createdAt
) {
	public static RecommendAuctionResponse of(
		Long auctionId,
		String title,
		String dong,
		int currentBiddingPrice,
		String imgUrl,
		int bookmarkCount,
		int biddingCount,
		String createdAt
	) {
		return new RecommendAuctionResponse(
			 auctionId, title, dong, currentBiddingPrice, imgUrl, bookmarkCount, biddingCount, createdAt);
	}
}
