package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class UserFixture {

	static final String PASSWORD = "password123";
	static final String NICKNAME = "nickname123";
	static final String PROFILE_IMAGE_URL =
		"https://lh3.googleusercontent.com/a/ACg8ocI5mIsHlnobowJ34VO9ZN8G31hlB4OBBRo_JoWItp5Vyg=s288-c-no";
	static final Address address = Address.of("서울시", "구로구", "가리봉동");

	public static User user1() {
		return User.getTestUser(1L, "hello1@naver.com", PASSWORD, NICKNAME, address, PROFILE_IMAGE_URL);
	}

	public static User user2() {
		return User.getTestUser(2L, "hello2@naver.com", PASSWORD, NICKNAME, address, PROFILE_IMAGE_URL);
	}

	public static User user(Long id, String email) {
		return User.getTestUser(id, email, PASSWORD, NICKNAME, address, PROFILE_IMAGE_URL);
	}
}
