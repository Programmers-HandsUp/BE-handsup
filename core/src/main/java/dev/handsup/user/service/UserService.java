package dev.handsup.user.service;

import static dev.handsup.user.exception.UserErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.UserJoinRequest;
import dev.handsup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EncryptHelper encryptHelper;

	private void validateDuplicateEmail(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new ValidationException(DUPLICATED_EMAIL);
		}
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID));
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_EMAIL));
	}

	@Transactional
	public Long join(UserJoinRequest userJoinRequest) {
		validateDuplicateEmail(userJoinRequest.email());
		String password = userJoinRequest.password();
		String encryptedPassword = encryptHelper.encrypt(password);

		Address address = Address.builder()
			.si(userJoinRequest.si())
			.gu(userJoinRequest.gu())
			.dong(userJoinRequest.dong())
			.build();

		User user = User.builder()
			.email(userJoinRequest.email())
			.password(encryptedPassword)
			.nickname(userJoinRequest.nickname())
			.address(address)
			.profileImageUrl(userJoinRequest.profileImageUrl())
			.build();

		return userRepository.save(user).getId();
	}
}
