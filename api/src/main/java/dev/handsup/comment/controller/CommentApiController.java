package dev.handsup.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.comment.dto.request.RegisterCommentRequest;
import dev.handsup.comment.dto.response.CommentResponse;
import dev.handsup.comment.service.CommentService;
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

	// TODO: 3/19/24 경매 아이디로 댓글 조회

}
