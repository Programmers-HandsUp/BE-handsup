package dev.handsup.user.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class UserMapper {

	public static User toUser(JoinUserRequest request, EncryptHelper encryptHelper) {
		String password = request.password();
		String encryptedPassword = encryptHelper.encrypt(password);

		Address address = Address.builder()
			.si(request.si())
			.gu(request.gu())
			.dong(request.dong())
			.build();

		return User.builder()
			.email(request.email())
			.password(encryptedPassword)
			.nickname(request.nickname())
			.address(address)
			.profileImageUrl(request.profileImageUrl())
			.build();
	}
}
