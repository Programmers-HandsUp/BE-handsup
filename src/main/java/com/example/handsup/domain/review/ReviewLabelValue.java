package com.example.handsup.domain.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewLabelValue {
	FAST_RESPONSE("응답이 빨라요."),
	CHEAP("저렴하게 구매했어요."),
	MANNER("친절하고 매너있어요"),
	TIME("시간 약속을 잘 지켜요");
	private final String description;
}
