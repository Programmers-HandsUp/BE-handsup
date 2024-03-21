package dev.handsup.user.dto.response;

public record UserBuyHistoryResponse(

	Long auctionId,
	String auctionTitle,
	String auctionImageUrl,
	String auctionCreatedAt,
	int winningPrice,
	String auctionStatus
) {
	public static UserBuyHistoryResponse of(
		Long auctionId,
		String auctionTitle,
		String auctionImageUrl,
		String auctionCreatedAt,
		int winningPrice,
		String auctionStatus
	) {
		return new UserBuyHistoryResponse(
			auctionId,
			auctionTitle,
			auctionImageUrl,
			auctionCreatedAt,
			winningPrice,
			auctionStatus
		);
	}
}
