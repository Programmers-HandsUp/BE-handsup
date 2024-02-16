package dev.handsup.review.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

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

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "review_id")
	private Long id;

	@Column(name = "score")
	private int score;

	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User writer;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auction_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Auction auction;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "review_label_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ReviewLabel reviewLabel;

	@Builder
	public Review(int score, String content, User writer, Auction auction, ReviewLabel reviewLabel) {
		this.score = score;
		this.content = content;
		this.writer = writer;
		this.auction = auction;
		this.reviewLabel = reviewLabel;
	}
}
