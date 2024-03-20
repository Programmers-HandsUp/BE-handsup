package dev.handsup.comment.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.comment.dto.request.RegisterCommentRequest;
import dev.handsup.comment.dto.response.CommentResponse;
import dev.handsup.comment.service.CommentService;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
public class CommentApiController {

	private final CommentService commentService;

	@Operation(summary = "댓글 등록 API", description = "경매에 댓글을 등록한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping("{auctionId}/comments")
	public ResponseEntity<CommentResponse> registerAuctionComment(
		@PathVariable("auctionId") Long auctionId,
		@Valid @RequestBody RegisterCommentRequest request,
		@Parameter(hidden = true) @JwtAuthorization User writer
	) {
		CommentResponse response = commentService.registerAuctionComment(auctionId, request, writer);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@Operation(summary = "댓글 조회 API", description = "한 경매에 대한 댓글을 모두 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("{auctionId}/comments")
	public ResponseEntity<PageResponse<CommentResponse>> getAuctionComments(
		@PathVariable("auctionId") Long auctionId,
		Pageable pageable
	) {
		PageResponse<CommentResponse> response = commentService.getAuctionComments(auctionId, pageable);
		return ResponseEntity.ok(response);
	}

}
