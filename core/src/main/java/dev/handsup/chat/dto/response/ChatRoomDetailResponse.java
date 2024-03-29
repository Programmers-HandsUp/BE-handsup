package dev.handsup.chat.dto.response;

public record ChatRoomDetailResponse(
	Long chatRoomId,
	Long auctionId,
	Long currentBiddingId,
	int currentBiddingPrice,
	String tradingStatus,
	String auctionTitle,
	String auctionImageUrl,
	Long receiverId,
	String receiverNickName,
	int receiverScore,
	String receiverProfileImageUrl
) {
	public static ChatRoomDetailResponse of(
		Long chatRoomId,
		Long auctionId,
		Long currentBiddingId,
		int currentBiddingPrice,
		String tradingStatus,
		String auctionTitle,
		String auctionImageUrl,
		Long receiverId,
		String receiverNickName,
		int receiverScore,
		String receiverProfileImageUrl
	) {
		return new ChatRoomDetailResponse(
			chatRoomId,
			auctionId,
			currentBiddingId,
			currentBiddingPrice,
			tradingStatus,
			auctionTitle,
			auctionImageUrl,
			receiverId,
			receiverNickName,
			receiverScore,
			receiverProfileImageUrl
		);
	}
}
