package dev.handsup.auction.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterAuctionRequest(

	@NotBlank(message = "title 값이 공백입니다.")
	String title,
	@NotBlank(message = "productCategory 값이 공백입니다.")
	String productCategory,
	@NotNull(message = "initPrice 값이 공백입니다.")
	@Size(min = 1000, max = 100_000_000, message = "평가 점수는 -2 ~ 2 이어야 합니다.")
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
		String si,
		String gu,
		String dong
	) {
		return new RegisterAuctionRequest(
			title, productCategory, initPrice, endDate, productStatus, purchaseTime, description, tradeMethod, si, gu,
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
		String tradeMethod
	) {
		return new RegisterAuctionRequest(
			title, productCategory, initPrice, endDate, productStatus, purchaseTime, description, tradeMethod, null,
			null, null);
	}
}
