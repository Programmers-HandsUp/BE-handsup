package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.review.domain.Review;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ReviewFixture {

	static final int EVALUATION_SCORE = 2;
	static final String CONTENT = "저렴한 가격에 좋은 물건을 잘 구매했어요!";
	static final Auction AUCTION = AuctionFixture.auction();
	static final User WRITER = UserFixture.user();

	public static Review review() {
		return Review.of(EVALUATION_SCORE, CONTENT, AUCTION, WRITER);
	}

	public static Review review(Long reviewId) {
		return new Review(reviewId, EVALUATION_SCORE, CONTENT, AUCTION, WRITER);
	}

	public static Review review(String content, Auction auction) {
		return Review.of(EVALUATION_SCORE, content, auction, WRITER);
	}

	public static Review review(String content, Auction auction, User writer) {
		return Review.of(EVALUATION_SCORE, content, auction, writer);
	}
}
