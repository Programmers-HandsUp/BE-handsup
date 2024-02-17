package dev.handsup.auction.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterAuctionApiRequest(

	@NotBlank(message = "title 값이 공백입니다.")
	String title,
	@NotBlank(message = "category 값이 공백입니다.")
	String category,
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

	String si,
	String gu,
	String dong
) {
}
