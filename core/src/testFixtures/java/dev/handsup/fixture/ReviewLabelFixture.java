package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.ReviewLabelValue;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ReviewLabelFixture {

	static final String VALUE_MANNER = ReviewLabelValue.MANNER.getLabel();
	static final String VALUE_CHEAP = ReviewLabelValue.CHEAP_PRICE.getLabel();

	public static ReviewLabel reviewLabelManner() {
		return ReviewLabel.from(VALUE_MANNER);
	}

	public static ReviewLabel reviewLabelCheap() {
		return ReviewLabel.from(VALUE_CHEAP);
	}

	public static ReviewLabel reviewLabel(Long id, String value) {
		return new ReviewLabel(id, value);
	}
}
