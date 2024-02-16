package dev.handsup.review.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ReviewInterReviewLabel {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "review_inter_review_label_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "review_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Review review;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "review_label_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ReviewLabel reviewLabel;
}
