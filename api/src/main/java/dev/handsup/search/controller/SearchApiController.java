package dev.handsup.search.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.response.AuctionSearchResponse;
import dev.handsup.auction.dto.response.AuctionSimpleResponse;
import dev.handsup.auction.dto.response.RecommendAuctionResponse;
import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.search.dto.PopularKeywordsResponse;
import dev.handsup.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions/search")
public class SearchApiController {
	private final SearchService searchService;

	@NoAuth
	@Operation(summary = "경매 검색 API", description = "경매를 검색한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<PageResponse<AuctionSimpleResponse>> searchAuctions(
		@Valid @RequestBody AuctionSearchCondition condition,
		Pageable pageable) {
		PageResponse<AuctionSimpleResponse> response = searchService.searchAuctions(condition, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 검색 API", description = "경매를 검색한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/v2")
	public ResponseEntity<PageResponse<AuctionSearchResponse>> optimizedSearchAuctions(
		@Valid @RequestBody AuctionSearchCondition condition,
		Pageable pageable) {
		PageResponse<AuctionSearchResponse> response = searchService.optimizedSearchAuctions(condition, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "인기 검색어 조회 API", description = "인기 검색어를 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/popular")
	public ResponseEntity<PopularKeywordsResponse> getPopularKeywords() {
		PopularKeywordsResponse response = searchService.getPopularKeywords();
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 추천 API", description = "정렬 조건에 따라 경매를 추천한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/recommend")
	@Cacheable(cacheNames = "auctions")
	public ResponseEntity<PageResponse<RecommendAuctionResponse>> getRecommendAuctions(
		@RequestParam(value = "si", required = false) String si,
		@RequestParam(value = "gu", required = false) String gu,
		@RequestParam(value = "dong", required = false) String dong,
		Pageable pageable
	) {
		PageResponse<RecommendAuctionResponse> response = searchService.getRecommendAuctions(si, gu, dong, pageable);
		return ResponseEntity.ok(response);
	}
}
