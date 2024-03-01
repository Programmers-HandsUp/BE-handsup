package dev.handsup.auction.dto.response;

public record RecommendAuctionResponse(
	Long auctionId,
	String title,
	String dong,
	int currentBiddingPrice,
	String imgUrl,
	int bookmarkCount,
	int biddingCount,
	String createdAt,
	String endDate
) {
	public static RecommendAuctionResponse of(
		Long auctionId,
		String title,
		String dong,
		int currentBiddingPrice,
		String imgUrl,
		int bookmarkCount,
		int biddingCount,
		String createdAt,
		String endDate
	) {
		return new RecommendAuctionResponse(
			 auctionId, title, dong, currentBiddingPrice, imgUrl, bookmarkCount, biddingCount, createdAt, endDate);
	}
}
