package dev.handsup.user.dto.response;

public record UserBuyHistoryResponse(

	Long auctionId,
	String auctionTitle,
	String auctionImageUrl,
	String auctionCreatedAt,
	String auctionEndDateTime,
	Integer buyPrice,
	String auctionStatus
) {
	public static UserBuyHistoryResponse of(
		Long auctionId,
		String auctionTitle,
		String auctionImageUrl,
		String auctionCreatedAt,
		String auctionEndDateTime,
		Integer buyPrice,
		String auctionStatus
	) {
		return new UserBuyHistoryResponse(
			auctionId,
			auctionTitle,
			auctionImageUrl,
			auctionCreatedAt,
			auctionEndDateTime,
			buyPrice,
			auctionStatus
		);
	}
}
