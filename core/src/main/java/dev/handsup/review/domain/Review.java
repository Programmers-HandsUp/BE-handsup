package dev.handsup.review.domain;

import static dev.handsup.common.exception.CommonValidationError.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.springframework.util.Assert;

import dev.handsup.auction.domain.Auction;
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
public class Review extends TimeBaseEntity {

	private static final int MIN_EVALUATION_SCORE = -2;
	private static final int MAX_EVALUATION_SCORE = 2;
	private static final int MIN_LENGTH_CONTENT = 5;
	private static final int MAX_LENGTH_CONTENT = 140;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "review_id")
	private Long id;

	@Column(name = "evaluation_score", nullable = false)
	private int evaluationScore;

	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auction_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Auction auction;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "writer_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User writer;

	@Builder
	private Review(
		int evaluationScore,
		String content,
		Auction auction,
		User writer
	) {
		validateScore(evaluationScore);
		validateContent(content);
		this.evaluationScore = evaluationScore;
		this.content = content;
		this.auction = auction;
		this.writer = writer;
	}

	public static Review of(
		int evaluationScore,
		String content,
		Auction auction,
		User writer
	) {
		return Review.builder()
			.evaluationScore(evaluationScore)
			.content(content)
			.auction(auction)
			.writer(writer)
			.build();
	}

	private void validateScore(int evaluationScore) {
		Assert.isTrue(
			MIN_EVALUATION_SCORE <= evaluationScore && evaluationScore <= MAX_EVALUATION_SCORE,
			getRangeMessage("Comment", "evaluationScore", MIN_EVALUATION_SCORE, MAX_EVALUATION_SCORE)
		);
	}

	private void validateContent(String content) {
		Assert.hasText(content, getNotEmptyMessage("Review", "content"));
		Assert.isTrue(
			content.length() >= MIN_LENGTH_CONTENT && content.length() <= MAX_LENGTH_CONTENT,
			getRangeMessage("Comment", "content 의 글자 수", MIN_LENGTH_CONTENT, MAX_LENGTH_CONTENT)
		);
	}

	//==테스트용 생성자==//
	public Review(
		Long id,
		int evaluationScore,
		String content,
		Auction auction,
		User writer
	) {
		validateScore(evaluationScore);
		validateContent(content);
		this.id = id;
		this.evaluationScore = evaluationScore;
		this.content = content;
		this.auction = auction;
		this.writer = writer;
	}

}
