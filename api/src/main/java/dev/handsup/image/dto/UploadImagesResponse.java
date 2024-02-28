package dev.handsup.image.dto;

import java.util.List;

public record UploadImagesResponse(
	List<String> imageUrls
) {
	public static UploadImagesResponse from(List<String> imageUrls) {
		return new UploadImagesResponse(imageUrls);
	}
}
