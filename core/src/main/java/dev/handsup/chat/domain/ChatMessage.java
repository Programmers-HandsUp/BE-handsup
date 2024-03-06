package dev.handsup.chat.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.common.entity.TimeBaseEntity;
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
public class ChatMessage extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "chat_message_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "chat_room_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ChatRoom chatRoom;

	@Column(name = "sender_id")
	private Long senderId;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	@Builder
	public ChatMessage(ChatRoom chatRoom, Long senderId, String content) {
		this.chatRoom = chatRoom;
		this.senderId = senderId;
		this.content = content;
		this.isRead = false;
	}
}
