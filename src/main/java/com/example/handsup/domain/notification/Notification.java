package com.example.handsup.domain.notification;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.example.handsup.domain.user.User;

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
public class Notification {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

	@Column(name = "content")
	private String content;

	@Column(name = "is_read")
	private Boolean isRead;

	@Column(name = "type")
	private NotificationType type;

	@Builder
	public Notification(User user, String content, NotificationType type) {
		this.user = user;
		this.content = content;
		this.type = type;
		isRead = false;
	}
}
