package dev.handsup.comment.mapper;

import dev.handsup.auction.domain.Auction;
import dev.handsup.comment.domain.Comment;
import dev.handsup.comment.dto.request.RegisterCommentRequest;
import dev.handsup.comment.dto.response.CommentResponse;
import dev.handsup.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CommentMapper {

	public static Comment toComment(RegisterCommentRequest request, Auction auction, User writer) {
		return Comment.of(request.content(), auction, writer);
	}

	public static CommentResponse toCommentResponse(Comment comment) {
		User writer = comment.getWriter();
		return CommentResponse.of(
			comment.getWriter().getId(),
			comment.getWriter().getNickname(),
			comment.getWriter().getProfileImageUrl(),
			comment.getContent(),
			comment.getAuction().isSeller(writer),
			comment.getCreatedAt().toString()
		);
	}
}
