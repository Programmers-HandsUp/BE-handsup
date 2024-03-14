package dev.handsup.notification.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.common.entity.TimeBaseEntity;
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
public class Notification extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@Column(name = "sender_email", nullable = false)
	private String senderEmail;

	@Column(name = "receiver_email", nullable = false)
	private String receiverEmail;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "type", nullable = false)
	@Enumerated(STRING)
	private NotificationType type;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auction_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Auction auction;

	@Builder
	private Notification(
		String senderEmail,
		String receiverEmail,
		String content,
		NotificationType type,
		Auction auction
	) {
		this.senderEmail = senderEmail;
		this.receiverEmail = receiverEmail;
		this.content = content;
		this.type = type;
		this.auction = auction;
	}

	public static Notification of(
		String senderEmail,
		String receiverEmail,
		String content,
		NotificationType type,
		Auction auction
	) {
		return Notification.builder()
			.senderEmail(senderEmail)
			.receiverEmail(receiverEmail)
			.content(content)
			.type(type)
			.auction(auction)
			.build();
	}

}
