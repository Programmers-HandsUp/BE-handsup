package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.comment.domain.Comment;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class CommentFixture {

	public static Comment comment(Auction auction, User writer) {
		return Comment.of(
			"와",
			auction,
			writer
		);
	}
}
