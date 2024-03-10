package dev.handsup.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatMessageRequest(

	@NotNull(message = "발신자 아이디를 입력해주세요.")
	Long senderId,
	@NotBlank(message = "메시지 내용을 입력해주세요.")
	@Size(max = 300, message = "최대 300자까지 보낼 수 있습니다.")
	String content
) {
	public static ChatMessageRequest of(Long senderId, String content) {
		return new ChatMessageRequest(senderId, content);
	}
}
