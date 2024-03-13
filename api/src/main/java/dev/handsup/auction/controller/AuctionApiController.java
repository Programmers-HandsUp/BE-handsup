package dev.handsup.auction.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.dto.response.RecommendAuctionResponse;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "경매 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class AuctionApiController {

	private final AuctionService auctionService;

	@NoAuth
	@Operation(summary = "경매 등록 API", description = "경매를 등록한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<AuctionDetailResponse> registerAuction(
		@Valid @RequestBody RegisterAuctionRequest request,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		AuctionDetailResponse response = auctionService.registerAuction(request, user);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 상세 조회 API", description = "경매 상세 조회를 가져온다")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/{auctionId}")
	public ResponseEntity<AuctionDetailResponse> getAuctionDetail(@PathVariable("auctionId") Long auctionId) {
		AuctionDetailResponse response = auctionService.getAuctionDetail(auctionId);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 추천 API", description = "정렬 조건에 따라 경매를 추천한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/recommend")
	public ResponseEntity<PageResponse<RecommendAuctionResponse>> getRecommendAuctions(
		@RequestParam(value = "si", required = false) String si,
		@RequestParam(value = "gu", required = false) String gu,
		@RequestParam(value = "dong", required = false) String dong,
		Pageable pageable
	) {
		PageResponse<RecommendAuctionResponse> response = auctionService.getRecommendAuctions(si, gu, dong, pageable);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "유저 선호 카테고리 경매 조회 API", description = "유저가 선호하는 카테고리의 경매를 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/recommend/category")
	public ResponseEntity<PageResponse<RecommendAuctionResponse>> getUserPreferredCategoryAuctions(
		@Parameter(hidden = true) @JwtAuthorization User user,
		Pageable pageable
	) {
		PageResponse<RecommendAuctionResponse> response = auctionService.getUserPreferredCategoryAuctions(user, pageable);
		return ResponseEntity.ok(response);
	}
}
