package dev.handsup.auction.dto.response;

public record ChatRoomExistenceResponse(
	Boolean isExist
) {
	public static ChatRoomExistenceResponse from(Boolean isExist) {
		return new ChatRoomExistenceResponse(isExist);
	}
}
