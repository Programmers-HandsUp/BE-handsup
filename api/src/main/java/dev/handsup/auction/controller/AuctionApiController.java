package dev.handsup.auction.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
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
	public ResponseEntity<AuctionResponse> registerAuction(@Valid @RequestBody RegisterAuctionRequest request) {
		AuctionResponse response = auctionService.registerAuction(request);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 검색 API", description = "경매를 검색한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/search")
	public ResponseEntity<PageResponse<AuctionResponse>> searchAuctions(@Valid @RequestBody AuctionSearchCondition condition,
		Pageable pageable) {
		PageResponse<AuctionResponse> response = auctionService.searchAuctions(condition, pageable);
		return ResponseEntity.ok(response);
	}
}
