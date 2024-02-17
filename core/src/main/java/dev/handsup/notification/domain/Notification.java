package dev.handsup.notification.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
public class Notification {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	@Column(name = "type", nullable = false)
	@Enumerated(STRING)
	private NotificationType type;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auction_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Auction auction;

	@Builder
	public Notification(User user, String content, NotificationType type, Auction auction) {
		this.user = user;
		this.content = content;
		this.type = type;
		this.auction = auction;
		isRead = false;
	}
}