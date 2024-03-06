package dev.handsup.user.dto.request;

import static lombok.AccessLevel.*;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(access = PRIVATE)
public record JoinUserRequest(

	@NotBlank(message = "email 값이 공백입니다.")
	@Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){0,19}"
		+ "@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){0,19}[.][a-zA-Z]{2,3}$",
		message = "이메일 주소 양식을 확인해주세요.")
	String email,

	@NotBlank(message = "password 값이 공백입니다.")
	String password,

	@NotBlank(message = "nickname 값이 공백입니다.")
	String nickname,

	@NotBlank(message = "주소의 시 값이 공백입니다.")
	String si,

	@NotBlank(message = "주소의 구 값이 공백입니다.")
	String gu,

	@NotBlank(message = "주소의 동 값이 공백입니다.")
	String dong,

	@NotBlank(message = "profileImageUrl 은 필수입니다.")
	String profileImageUrl,

	List<Long> productCategoryIds
) {
	public static JoinUserRequest of(
		String email,
		String password,
		String nickname,
		String si,
		String gu,
		String dong,
		String profileImageUrl,
		List<Long> productCategoryIds
	) {
		return JoinUserRequest.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.si(si)
			.gu(gu)
			.dong(dong)
			.profileImageUrl(profileImageUrl)
			.productCategoryIds(productCategoryIds)
			.build();
	}
}
