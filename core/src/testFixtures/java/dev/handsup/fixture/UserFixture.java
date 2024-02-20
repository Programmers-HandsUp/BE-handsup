package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class UserFixture {

	static final String EMAIL = "hello123@naver.com";
	static final String PASSWORD = "password123";
	static final String NICKNAME = "nickname123";
	static final String PROFILE_IMAGE_URL =
		"https://lh3.googleusercontent.com/a/ACg8ocI5mIsHlnobowJ34VO9ZN8G31hlB4OBBRo_JoWItp5Vyg=s288-c-no";
	static final Address address = Address.builder()
		.si("서울시")
		.gu("구로구")
		.dong("가리봉동")
		.build();

	public static User user() {
		return User.of(EMAIL, PASSWORD, NICKNAME, address, PROFILE_IMAGE_URL);
	}

	public static User testUser(Long id) {
		return User.getTestUser(id, EMAIL, PASSWORD, NICKNAME, address, PROFILE_IMAGE_URL);
	}
}
