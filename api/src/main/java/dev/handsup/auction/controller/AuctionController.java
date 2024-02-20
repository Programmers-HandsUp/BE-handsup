package dev.handsup.auction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.dto.ApiAuctionMapper;
import dev.handsup.auction.dto.AuctionResponse;
import dev.handsup.auction.dto.RegisterAuctionApiRequest;
import dev.handsup.auction.dto.RegisterAuctionRequest;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.auth.annotation.NoAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "경매 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class AuctionController {

	private final AuctionService auctionService;

	@NoAuth
	@Operation(summary = "경매 등록 API", description = "경매를 등록한다")
	@PostMapping
	public ResponseEntity<AuctionResponse> registerAuction(@Valid @RequestBody RegisterAuctionApiRequest request) {
		RegisterAuctionRequest registerAuctionRequest = ApiAuctionMapper.toRegisterAuctionRequest(request);
		AuctionResponse response = auctionService.registerAuction(registerAuctionRequest);
		return ResponseEntity.ok(response);
	}

}
