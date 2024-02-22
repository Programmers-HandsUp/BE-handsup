package dev.handsup.bidding.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.repository.AuctionRepository;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.bidding.dto.BiddingApiMapper;
import dev.handsup.bidding.dto.RegisterBiddingApiRequest;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.RegisterBiddingResponse;
import dev.handsup.bidding.service.BiddingService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Bidding API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class BiddingApiController {

	private final BiddingService biddingService;
	//TODO 추후 AuctionService의 조회메서드로 변경
	private final AuctionRepository auctionRepository;

	@PostMapping("/{auctionId}/bids")
	@Operation(summary = "입찰 등록 API", description = "입찰한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<RegisterBiddingResponse> registerBidding(
		@PathVariable Long auctionId,
		@Valid @RequestBody RegisterBiddingApiRequest request,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		Optional<Auction> auction = auctionRepository.findById(auctionId);

		if(auction.isPresent()) {
			RegisterBiddingRequest registerBiddingRequest = BiddingApiMapper.toRegisterBiddingRequest(
				request,
				auction.get(),
				user
			);
			RegisterBiddingResponse response = biddingService.registerBidding(
				registerBiddingRequest
			);
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.badRequest().build();
	}
}
