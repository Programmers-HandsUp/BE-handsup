package dev.handsup.auction.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterAuctionRequest(

	@NotBlank(message = "title 값이 공백입니다.")
	String title,
	@NotBlank(message = "productCategory 값이 공백입니다.")
	String productCategory,
	@NotNull(message = "initPrice 값이 공백입니다.")
	@Min(value = 1000, message = "최소 금액은 1000원입니다.")
	@Max(value = 100000000, message = "최대 금액은 1억입니다.")
	int initPrice,

	@NotNull(message = "endDate 값이 공백입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd") //localDate 형식으로 받음
	LocalDate endDate,

	@NotBlank(message = "productStatus가 공백입니다.")
	String productStatus,

	@NotBlank(message = "purchaseTime이 공백입니다.")
	String purchaseTime,

	@NotBlank(message = "description이 공백입니다.")
	String description,

	@NotBlank(message = "tradeMethod가 공백입니다.")
	String tradeMethod,

	@NotNull(message = "이미지 주소는 필수값입니다.")
	List<String> imageUrls,
	String si,
	String gu,
	String dong
) {
	public static RegisterAuctionRequest of(
		String title,
		String productCategory,
		int initPrice,
		LocalDate endDate,
		String productStatus,
		String purchaseTime,
		String description,
		String tradeMethod,
		List<String> imgUrls,
		String si,
		String gu,
		String dong
	) {
		return new RegisterAuctionRequest(
			title, productCategory, initPrice, endDate, productStatus, purchaseTime, description, tradeMethod, imgUrls, si, gu,
			dong);
	}

	public static RegisterAuctionRequest of(
		String title,
		String productCategory,
		int initPrice,
		LocalDate endDate,
		String productStatus,
		String purchaseTime,
		String description,
		String tradeMethod,
		List<String> imgUrls
	) {
		return new RegisterAuctionRequest(
			title, productCategory, initPrice, endDate, productStatus, purchaseTime, description, tradeMethod, imgUrls, null,
			null, null);
	}
}