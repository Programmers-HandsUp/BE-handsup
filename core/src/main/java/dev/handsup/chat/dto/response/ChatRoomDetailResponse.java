package dev.handsup.chat.dto.response;

public record ChatRoomDetailResponse(
	Long chatRoomId,
	Long auctionId,
	String auctionTitle,
	Long receiverId,
	String receiverNickName,
	int receiverScore,
	String receiverProfileImageUrl
) {
	public static ChatRoomDetailResponse of(
		Long chatRoomId,
		Long auctionId,
		String auctionTitle,
		Long receiverId,
		String receiverNickName,
		int receiverScore,
		String receiverProfileImageUrl
	){
		return new ChatRoomDetailResponse(
			chatRoomId,
			auctionId,
			auctionTitle,
			receiverId,
			receiverNickName,
			receiverScore,
			receiverProfileImageUrl
		);
	}
}
