package dev.handsup.review.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewResponse;
import dev.handsup.review.service.ReviewService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
		@Parameter(hidden = true) @JwtAuthorization User writer
	) {
		ReviewResponse response = reviewService.registerReview(
			request,
			auctionId,
			writer
		);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{auctionId}/reviews")
	@Operation(summary = "경매 리뷰 조회 API", description = "해당 경매의 리뷰를 전체 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<PageResponse<ReviewResponse>> getReviewsOfAuction(
		@PathVariable Long auctionId,
		Pageable pageable
	) {
		PageResponse<ReviewResponse> response = reviewService.getReviewsOfAuction(auctionId, pageable);
		return ResponseEntity.ok(response);
	}
}
