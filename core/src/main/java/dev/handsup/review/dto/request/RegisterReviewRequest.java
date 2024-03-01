package dev.handsup.review.dto.request;

import static lombok.AccessLevel.*;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterReviewRequest(

	@NotNull(message = "evaluationScore")
	@Min(value = -2, message = "평가 점수는 -2 이상이어야 합니다.")
	@Max(value = 2, message = "평가 점수는 2 이하이어야 합니다.")
	int evaluationScore,

	@Size(max = 140, message = "리뷰의 글자 수는 최대 140자 입니다.")
	String content,

	@NotNull(message = "리뷰 라벨 ID 목록은 필수입니다.")
	List<Long> reviewLabelIds
) {
	public static RegisterReviewRequest of(
		int evaluationScore,
		String content,
		List<Long> reviewLabelIds
	) {
		return RegisterReviewRequest.builder()
			.evaluationScore(evaluationScore)
			.content(content)
			.reviewLabelIds(reviewLabelIds)
			.build();
	}
}
