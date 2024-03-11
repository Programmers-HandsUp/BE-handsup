package dev.handsup.notification.domain;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

	@Builder
	private Notification(
		String senderEmail,
		String receiverEmail,
		String content,
		NotificationType type
	) {
		this.senderEmail = senderEmail;
		this.receiverEmail = receiverEmail;
		this.content = content;
		this.type = type;
	}

	public static Notification of(
		String senderEmail,
		String receiverEmail,
		String content,
		NotificationType type
	) {
		return Notification.builder()
			.senderEmail(senderEmail)
			.receiverEmail(receiverEmail)
			.content(content)
			.type(type)
			.build();
	}

}
