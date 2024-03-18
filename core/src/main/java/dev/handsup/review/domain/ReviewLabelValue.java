package dev.handsup.review.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewLabelValue {

	FAST_RESPONSE("응답이 빨라요"),
	PRICE("가격이 착해요"),
	MANNER("친절하고 매너 있어요"),
	PROMISE("약속을 잘 지켜요"),
	DETAILED_DESCRIPTION("설명을 잘 해줘요"),
	ACCURATE_CONDITION("물품 상태 사진과 같아요"),
	DIRECT_TRADING("직접 와서 거래해요");

	private final String description;
}
