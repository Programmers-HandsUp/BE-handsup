package dev.handsup.review.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewLabelValue {

	FAST_RESPONSE("응답이 빨라요."),
	CHEAP("저렴하게 구매했어요."),
	MANNER("친절하고 매너있어요"),
	TIME("시간 약속을 잘 지켜요"),
	DETAILED_DESCRIPTION("물품 설명이 자세해요."),
	ACCURATE_CONDITION("물품 상태가 설명한 것과 같아요.");

	private final String description;
}
