package dev.handsup.auction.domain.product.product_category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategoryValue {

	DIGITAL_DEVICES("디지털 기기"),
	FURNITURE_INTERIOR("가구/인테리어"),
	FASHION_ACCESSORIES("패션/잡화"),
	HOUSEHOLD_APPLIANCES("생활가전"),
	LIVING_KITCHEN("생활/주방"),
	SPORTS_LEISURE("스포츠/레저"),
	HOBBIES_GAMES_RECORDS("취미/게임/음반"),
	BEAUTY_COSMETICS("뷰티/미용"),
	PET_SUPPLIES("반려동물용품"),
	TICKETS_VOUCHERS("티켓/교환권"),
	BOOKS("도서"),
	CHILDREN_BOOKS("유아도서"),
	OTHER_USED_GOODS("기타 중고 물품");

	private final String label;
}
