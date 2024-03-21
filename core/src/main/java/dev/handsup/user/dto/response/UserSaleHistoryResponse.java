package dev.handsup.user.dto.response;

public record UserSaleHistoryResponse(

	Long auctionId,
	String auctionTitle,
	String auctionImageUrl,
	String auctionCreatedAt,
	String auctionEndDateTime,
	int maxBiddingPrice,
	int salePrice,
	String auctionStatus
) {
	public static UserSaleHistoryResponse of(
		Long auctionId,
		String auctionTitle,
		String auctionImageUrl,
		String auctionCreatedAt,
		String auctionEndDateTime,
		int maxBiddingPrice,
		int salePrice,
		String auctionStatus
	) {
		return new UserSaleHistoryResponse(
			auctionId,
			auctionTitle,
			auctionImageUrl,
			auctionCreatedAt,
			auctionEndDateTime,
			maxBiddingPrice,
			salePrice,
			auctionStatus
		);
	}
}
