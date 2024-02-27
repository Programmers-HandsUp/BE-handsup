package dev.handsup.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.image.dto.UploadImagesRequest;
import dev.handsup.image.dto.UploadImagesResponse;
import dev.handsup.image.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
	private final S3Service s3Service;

	@NoAuth
	@PostMapping
	public ResponseEntity<UploadImagesResponse> uploadImages(
		@ModelAttribute @Valid UploadImagesRequest uploadImagesRequest
	) {
		List<String> imgUrls = s3Service.uploadImages(uploadImagesRequest.images());
		return ResponseEntity.ok(UploadImagesResponse.from(imgUrls));
	}
}
