package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class UserReviewLabelFixture {

	static final User user = UserFixture.user();

	public static UserReviewLabel userReviewLabel(Long id, ReviewLabel reviewLabel) {
		return new UserReviewLabel(id, reviewLabel);
	}
}
