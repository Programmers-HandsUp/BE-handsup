package dev.handsup.bookmark.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.bookmark.dto.CheckBookmarkStatusResponse;
import dev.handsup.bookmark.dto.EditBookmarkResponse;
import dev.handsup.bookmark.dto.FindUserBookmarkResponse;
import dev.handsup.bookmark.service.BookmarkService;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "북마크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions/bookmarks")
public class BookmarkApiController {

	private final BookmarkService bookmarkService;

	@Operation(summary = "북마크 추가 API", description = "북마크를 추가한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("/{auctionId}")
	public ResponseEntity<EditBookmarkResponse> addBookmark(
		@PathVariable Long auctionId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		EditBookmarkResponse response = bookmarkService.addBookmark(user, auctionId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "북마크 삭제 API", description = "북마크를 삭제한다")
	@ApiResponse(useReturnTypeSchema = true)
	@DeleteMapping("/{auctionId}")
	public ResponseEntity<EditBookmarkResponse> deleteBookmark(
		@PathVariable Long auctionId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		EditBookmarkResponse response = bookmarkService.cancelBookmark(user, auctionId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "북마크 여부 조회 API", description = "북마크 여부를 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/{auctionId}")
	public ResponseEntity<CheckBookmarkStatusResponse> checkBookmarkStatus(
		@PathVariable Long auctionId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		CheckBookmarkStatusResponse response = bookmarkService.checkBookmarkStatus(user, auctionId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "북마크 모두 조회 API", description = "북마크 한 경매글을 모두 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping
	public ResponseEntity<PageResponse<FindUserBookmarkResponse>> findUserBookmarks(
		@Parameter(hidden = true) @JwtAuthorization User user,
		Pageable pageable
	) {
		PageResponse<FindUserBookmarkResponse> response = bookmarkService.findUserBookmarks(user, pageable);
		return ResponseEntity.ok(response);
	}
}
