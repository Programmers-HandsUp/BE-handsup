package dev.handsup.auth.domain;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import dev.handsup.auth.exception.AuthErrorCode;
import dev.handsup.common.exception.NotFoundException;

@Component
public class BcryptImpl implements EncryptHelper {

	@Override
	public String encrypt(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	@Override
	public boolean isMatch(String plainPassword, String hashedPassword) {
		try {
			return BCrypt.checkpw(plainPassword, hashedPassword);
		} catch (Exception e) {
			throw new NotFoundException(AuthErrorCode.FAILED_LOGIN_BY_ANYTHING);
		}
	}
}
