package dev.handsup.review.dto.request;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterReviewRequest(

	@NotNull(message = "evaluationScore")
	@Size(min = -2, max = 2, message = "평가 점수는 -2 ~ 2 이어야 합니다.")
	int evaluationScore,

	String content
) {
	public static RegisterReviewRequest of(int evaluationScore, String content) {
		return RegisterReviewRequest.builder()
			.evaluationScore(evaluationScore)
			.content(content)
			.build();
	}
}
