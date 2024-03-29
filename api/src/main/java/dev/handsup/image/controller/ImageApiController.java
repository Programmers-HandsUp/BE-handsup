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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "이미지 업로드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageApiController {

	private final S3Service s3Service;

	@NoAuth
	@Operation(summary = "이미지 파일 리스트 전송 API", description = "이미지 파일을 전송하고 URL을 반환받는다.")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<UploadImagesResponse> uploadImages(
		@ModelAttribute @Valid UploadImagesRequest uploadImagesRequest
	) {
		List<String> imageUrls = s3Service.uploadImages(uploadImagesRequest.images());
		return ResponseEntity.ok(UploadImagesResponse.from(imageUrls));
	}
}
