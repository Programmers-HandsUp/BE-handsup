package dev.handsup.bidding.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.RegisterBiddingResponse;
import dev.handsup.bidding.service.BiddingService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Bidding API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class BiddingApiController {

	private final BiddingService biddingService;

	@PostMapping("/{auctionId}/bids")
	@Operation(summary = "입찰 등록 API", description = "입찰한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<RegisterBiddingResponse> registerBidding(
		@PathVariable Long auctionId,
		@RequestBody RegisterBiddingRequest request,
		@Parameter(hidden = true) @JwtAuthorization User bidder
	) {
		RegisterBiddingResponse response = biddingService.registerBidding(
			request,
			auctionId,
			bidder
		);
		return ResponseEntity.ok(response);
	}
}
