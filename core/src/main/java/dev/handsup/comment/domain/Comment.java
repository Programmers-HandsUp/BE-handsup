package dev.handsup.comment.domain;

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
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(name = "content", nullable = false)
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
	private Comment(String content, Auction auction, User writer) {
		this.content = content;
		this.auction = auction;
		this.writer = writer;
	}

	// 테스트용 생성자
	private Comment(Long id, String content, Auction auction, User writer) {
		this.id = id;
		this.content = content;
		this.auction = auction;
		this.writer = writer;
	}

	public static Comment of(String content, Auction auction, User writer) {
		return Comment.builder()
			.content(content)
			.auction(auction)
			.writer(writer)
			.build();
	}

	public static Comment getTestComment(
		Long id, String content, Auction auction, User writer
	) {
		return new Comment(id, content, auction, writer);
	}

}
