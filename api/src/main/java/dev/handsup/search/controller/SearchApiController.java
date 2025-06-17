package dev.handsup.search.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.search.dto.AuctionSearchCondition;
import dev.handsup.search.dto.AuctionSearchResponse;
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
public class SearchApiController {
	private final SearchService searchService;

	@NoAuth
	@Operation(summary = "경매 검색 API", description = "경매를 검색한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/api/auctions/search")
	public ResponseEntity<PageResponse<AuctionSearchResponse>> searchAuctions(
		@Valid @RequestBody AuctionSearchCondition condition,
		Pageable pageable) {
		PageResponse<AuctionSearchResponse> response = searchService.searchAuctions(condition, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "최적화된 경매 검색 API", description = "경매를 검색한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/api/v2/auctions/search")
	public ResponseEntity<PageResponse<AuctionSearchResponse>> searchAuctionsV2(
		@Valid @RequestBody AuctionSearchCondition condition,
		Pageable pageable) {
		PageResponse<AuctionSearchResponse> response = searchService.searchAuctionsV2(condition, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "인기 검색어 조회 API", description = "인기 검색어를 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/api/auctions/search/popular")
	public ResponseEntity<PopularKeywordsResponse> getPopularKeywords() {
		PopularKeywordsResponse response = searchService.getPopularKeywords();
		return ResponseEntity.ok(response);
	}
}
