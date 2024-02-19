package dev.handsup.fixture;

import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;

public final class UserFixture {

	static final String email = "hello123@naver.com";
	static final String password = "password123";
	static final String nickname = "nickname123";
	static final String profileImageUrl =
		"https://lh3.googleusercontent.com/a/ACg8ocI5mIsHlnobowJ34VO9ZN8G31hlB4OBBRo_JoWItp5Vyg=s288-c-no";
	static final Address address = Address.builder()
		.si("서울시")
		.gu("구로구")
		.dong("가리봉동")
		.build();

	// 인스턴스화 방지
	private UserFixture() {
	}

	public static User getUser() {
		return User.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.address(address)
			.profileImageUrl(profileImageUrl)
			.build();
	}

	public static User getTestUser(Long id) {
		return User.getTestUser(id, email, password, nickname, address, profileImageUrl);
	}
}
