package dev.handsup.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewResponse;
import dev.handsup.review.service.ReviewService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class ReviewApiController {

	private final ReviewService reviewService;

	@PostMapping("/{auctionId}/reviews")
	@Operation(summary = "리뷰 등록 API", description = "리뷰를 등록한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<ReviewResponse> registerReview(
		@Valid @RequestBody RegisterReviewRequest request,
		@PathVariable Long auctionId,
		@JwtAuthorization User writer
	) {
		ReviewResponse response = reviewService.registerReview(
			request,
			auctionId,
			writer
		);
		return ResponseEntity.ok(response);
	}
}
