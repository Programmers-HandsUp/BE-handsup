package dev.handsup.user.dto.response;

public record UserSaleHistoryResponse(

	Long auctionId,
	String auctionTitle,
	String auctionImageUrl,
	String auctionCreatedAt,
	int maxBiddingPrice,
	int winningPrice,
	String auctionStatus
) {
	public static UserSaleHistoryResponse of(
		Long auctionId,
		String auctionTitle,
		String auctionImageUrl,
		String auctionCreatedAt,
		int maxBiddingPrice,
		int winningPrice,
		String auctionStatus
	) {
		return new UserSaleHistoryResponse(
			auctionId,
			auctionTitle,
			auctionImageUrl,
			auctionCreatedAt,
			maxBiddingPrice,
			winningPrice,
			auctionStatus
		);
	}
}
