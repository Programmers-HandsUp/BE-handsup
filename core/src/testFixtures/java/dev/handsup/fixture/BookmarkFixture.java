package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.Bookmark;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BookmarkFixture {
	public static Bookmark bookmark(User user, Auction auction) {
		return Bookmark.of(
			user,
			auction
		);
	}
}
