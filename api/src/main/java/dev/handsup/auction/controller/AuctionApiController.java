package dev.handsup.auction.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.auction.dto.response.CheckBookmarkStatusResponse;
import dev.handsup.auction.dto.response.EditBookmarkResponse;
import dev.handsup.auction.dto.response.FindUserBookmarkResponse;
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
	public ResponseEntity<AuctionResponse> registerAuction(@Valid @RequestBody RegisterAuctionRequest request) {
		AuctionResponse response = auctionService.registerAuction(request);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "경매 검색 API", description = "경매를 검색한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/search")
	public ResponseEntity<PageResponse<AuctionResponse>> searchAuctions(
		@RequestBody AuctionSearchCondition condition,
		Pageable pageable) {
		PageResponse<AuctionResponse> response = auctionService.searchAuctions(condition, pageable);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "북마크 추가 API", description = "북마크를 추가한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/bookmarks/{auctionId}")
	public ResponseEntity<EditBookmarkResponse> addBookmark(
		@PathVariable Long auctionId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		EditBookmarkResponse response = auctionService.addBookmark(user, auctionId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "북마크 삭제 API", description = "북마크를 삭제한다")
	@ApiResponse(useReturnTypeSchema = true)
	@DeleteMapping("/bookmarks/{auctionId}")
	public ResponseEntity<EditBookmarkResponse> deleteBookmark(
		@PathVariable Long auctionId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		EditBookmarkResponse response = auctionService.cancelBookmark(user, auctionId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "북마크 여부 조회 API", description = "북마크 여부를 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/bookmarks/{auctionId}")
	public ResponseEntity<CheckBookmarkStatusResponse> checkBookmarkStatus(
		@PathVariable Long auctionId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		CheckBookmarkStatusResponse response = auctionService.checkBookmarkStatus(user, auctionId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "북마크 모두 조회 API", description = "북마크 한 경매글을 모두 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/bookmarks")
	public ResponseEntity<PageResponse<FindUserBookmarkResponse>> findUserBookmarks(
		@Parameter(hidden = true) @JwtAuthorization User user,
		Pageable pageable
	) {
		PageResponse<FindUserBookmarkResponse> response = auctionService.findUserBookmarks(user, pageable);
		return ResponseEntity.ok(response);
	}

}
