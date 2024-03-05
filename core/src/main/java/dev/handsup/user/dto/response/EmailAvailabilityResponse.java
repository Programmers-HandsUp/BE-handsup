package dev.handsup.user.dto.response;

public record EmailAvailabilityResponse(

	boolean isAvailable
) {
	public static EmailAvailabilityResponse from(boolean isAvailable) {
		return new EmailAvailabilityResponse(isAvailable);
	}
}
