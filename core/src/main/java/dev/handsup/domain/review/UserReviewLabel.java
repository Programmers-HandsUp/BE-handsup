package dev.core.domain.review;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.core.common.entity.TimeBaseEntity;
import dev.core.domain.user.User;

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
	@JoinColumn(name = "review_label_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ReviewLabel reviewLabel;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

	@Column(name = "count")
	private int count;

	@Builder
	public UserReviewLabel(ReviewLabel reviewLabel, User user) {
		this.reviewLabel = reviewLabel;
		this.user = user;
		count = 0;
	}
}
