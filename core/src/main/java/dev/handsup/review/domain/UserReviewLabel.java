package dev.handsup.review.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.common.entity.TimeBaseEntity;
import dev.handsup.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class UserReviewLabel extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_review_label_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "review_label_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ReviewLabel reviewLabel;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

	@Column(name = "count", nullable = false)
	private int count = 0;

	@Builder
	private UserReviewLabel(ReviewLabel reviewLabel, User user) {
		this.reviewLabel = reviewLabel;
		this.user = user;
	}

	public UserReviewLabel(Long id, ReviewLabel reviewLabel) {
		this.id = id;
		this.reviewLabel = reviewLabel;
	}

	public static UserReviewLabel of(ReviewLabel reviewLabel, User user) {
		return UserReviewLabel.builder()
			.reviewLabel(reviewLabel)
			.user(user)
			.build();
	}

	public void increaseCount() {
		this.count++;
	}
}
