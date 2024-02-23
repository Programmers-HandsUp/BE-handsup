package dev.handsup.auction.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.dto.response.AuctionSimpleResponse;
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
		@Parameter(hidden = true) @JwtAuthorization User user) {
		AuctionDetailResponse response = auctionService.registerAuction(request, user);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 상세 조회 API", description = "경매 상세 조회를 가져온다")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/{auctionId}")
	public ResponseEntity<AuctionDetailResponse> getAuctionDetail(@PathVariable("auctionId") Long auctionId){
		AuctionDetailResponse response = auctionService.getAuctionDetail(auctionId);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 검색 API", description = "경매를 검색한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/search")
	public ResponseEntity<PageResponse<AuctionSimpleResponse>> searchAuctions(@RequestBody AuctionSearchCondition condition,
		Pageable pageable) {
		PageResponse<AuctionSimpleResponse> response = auctionService.searchAuctions(condition, pageable);
		return ResponseEntity.ok(response);
	}
}
