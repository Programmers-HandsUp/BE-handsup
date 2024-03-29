package dev.handsup.bidding.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.BiddingResponse;
import dev.handsup.bidding.service.BiddingService;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "입찰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class BiddingApiController {

	private final BiddingService biddingService;

	@PostMapping("/{auctionId}/bids")
	@Operation(summary = "입찰 등록 API", description = "사용자가 경매에 입찰을 등록한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<BiddingResponse> registerBidding(
		@PathVariable Long auctionId,
		@Valid @RequestBody RegisterBiddingRequest request,
		@Parameter(hidden = true) @JwtAuthorization User bidder
	) {
		BiddingResponse response = biddingService.registerBidding(request, auctionId, bidder);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/{auctionId}/bids")
	@Operation(summary = "입찰 목록 전체 조회 API", description = "한 경매의 모든 입찰 목록을 입찰가 기준 내림차순으로 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<PageResponse<BiddingResponse>> getBidsOfAuction(
		@PathVariable Long auctionId,
		Pageable pageable
	) {
		PageResponse<BiddingResponse> response = biddingService.getBidsOfAuction(auctionId, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/{auctionId}/bids/top3")
	@Operation(summary = "입찰 목록 상위 3개 조회 API", description = "한 경매의 입찰 목록 중에서 입찰가 기준 내림차순으로 3개를 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<PageResponse<BiddingResponse>> getTop3BidsForAuction(
		@PathVariable Long auctionId
	) {
		Pageable pageable = PageRequest.of(0, 3);
		PageResponse<BiddingResponse> response = biddingService.getBidsOfAuction(auctionId, pageable);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/bids/{biddingId}/complete")
	@Operation(summary = "판매자 거래 완료 API", description = "판매자는 거래 중인 입찰자와 거래를 완료 한다.")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<BiddingResponse> completeTrading(
		@PathVariable("biddingId") Long biddingId,
		@JwtAuthorization User user
	) {
		BiddingResponse response = biddingService.completeTrading(biddingId, user);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/bids/{biddingId}/cancel")
	@Operation(summary = "판매자 거래 취소 API", description = "판매자는 거래 중인 입찰자와 거래를 취소한다.")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<BiddingResponse> cancelTrading(
		@PathVariable("biddingId") Long biddingId,
		@JwtAuthorization User user
	) {
		BiddingResponse response = biddingService.cancelTrading(biddingId, user);
		return ResponseEntity.ok(response);
	}
}
