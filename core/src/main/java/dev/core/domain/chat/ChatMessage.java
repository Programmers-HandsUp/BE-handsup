package dev.core.domain.chat;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.core.common.entity.TimeBaseEntity;

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

	@Column(name = "content")
	private String content;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "chat_room_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ChatRoom chatRoom;

	@Column(name = "is_read")
	private Boolean isRead;

	@Builder
	public ChatMessage(String content, ChatRoom chatRoom) {
		this.content = content;
		this.chatRoom = chatRoom;
		isRead = false;
	}
}
