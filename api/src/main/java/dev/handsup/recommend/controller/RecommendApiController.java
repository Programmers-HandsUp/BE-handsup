package dev.handsup.recommend.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.recommend.dto.RecommendAuctionResponse;
import dev.handsup.recommend.service.RecommendService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "홈 추천 API")
@RestController
@RequiredArgsConstructor
public class RecommendApiController {
	private final RecommendService recommendService;

	@NoAuth
	@Operation(summary = "경매 추천 API", description = "정렬 조건에 따라 경매를 추천한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/auctions/recommend")
	@Cacheable(cacheNames = "auctions")
	public ResponseEntity<PageResponse<RecommendAuctionResponse>> getRecommendAuctions(
		@RequestParam(value = "si", required = false) String si,
		@RequestParam(value = "gu", required = false) String gu,
		@RequestParam(value = "dong", required = false) String dong,
		Pageable pageable
	) {
		PageResponse<RecommendAuctionResponse> response = recommendService.getRecommendAuctions(si, gu, dong, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "성능 개선된 경매 추천 API", description = "정렬 조건에 따라 경매를 추천한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/v2/auctions/recommend")
	@Cacheable(cacheNames = "auctions")
	public ResponseEntity<PageResponse<RecommendAuctionResponse>> getRecommendAuctionsV2(
		@RequestParam(value = "si", required = false) String si,
		@RequestParam(value = "gu", required = false) String gu,
		@RequestParam(value = "dong", required = false) String dong,
		Pageable pageable
	) {
		PageResponse<RecommendAuctionResponse> response = recommendService.getRecommendAuctionsV2(si, gu, dong, pageable);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "유저 선호 카테고리 경매 조회 API", description = "유저가 선호하는 카테고리의 경매를 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/auctions/recommend/category")
	public ResponseEntity<PageResponse<RecommendAuctionResponse>> getUserPreferredCategoryAuctions(
		@Parameter(hidden = true) @JwtAuthorization User user,
		Pageable pageable
	) {
		PageResponse<RecommendAuctionResponse> response = recommendService.getUserPreferredCategoryAuctions(user,
			pageable);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "성능 개선된 유저 선호 카테고리 경매 조회 API", description = "유저가 선호하는 카테고리의 경매를 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/v2/auctions/recommend/category")
	public ResponseEntity<PageResponse<RecommendAuctionResponse>> getUserPreferredCategoryAuctionsV2(
		@Parameter(hidden = true) @JwtAuthorization User user,
		Pageable pageable
	) {
		PageResponse<RecommendAuctionResponse> response = recommendService.getUserPreferredCategoryAuctionsV2(user,
			pageable);
		return ResponseEntity.ok(response);
	}
}

