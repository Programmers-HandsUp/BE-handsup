package dev.handsup.review.domain;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ReviewLabel {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "review_label_id")
	private Long id;

	@Column(name = "review_label_value", nullable = false)
	@Enumerated(STRING)
	private ReviewLabelValue value;

	@Builder
	private ReviewLabel(ReviewLabelValue value) {
		this.value = value;
	}

	public static ReviewLabel from(ReviewLabelValue value) {
		return ReviewLabel.builder()
			.value(value)
			.build();
	}

	//==테스트용 생성자==//
	private ReviewLabel(Long id, ReviewLabelValue value) {
		this.id = id;
		this.value = value;
	}

	public static ReviewLabel getTestReviewLabel(Long id, ReviewLabelValue value) {
		return new ReviewLabel(id, value);
	}
}
