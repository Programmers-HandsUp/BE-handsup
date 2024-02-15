package dev.handsup.auth.domain;

public interface EncryptHelper {
	String encrypt(String plainPassword);
	boolean isMatch(String plainPassword, String hashedPassword);
}
