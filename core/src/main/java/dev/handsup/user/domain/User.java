package dev.handsup.user.domain;

import static dev.handsup.common.exception.CommonValidationError.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import org.springframework.util.Assert;

import dev.handsup.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class User extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "score")
	private int score;

	@Embedded
	private Address address;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(name = "report_count")
	private int reportCount;

	@Builder
	public User(
		String email,
		String password,
		String nickname,
		Address address,
		String profileImageUrl
	) {
		validateUser(email, password, nickname, address, profileImageUrl);
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.score = 100;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
		this.reportCount = 0;
	}

	private void validateUser(
		String email,
		String password,
		String nickname,
		Address address,
		String profileImageUrl
	) {
		Assert.hasText(email, getNotEmptyMessage("User", "email"));
		Assert.hasText(password, getNotEmptyMessage("User", "password"));
		Assert.hasText(nickname, getNotEmptyMessage("User", "nickname"));
		Assert.notNull(address, getNotNullMessage("User", "address"));
		Assert.hasText(profileImageUrl, getNotEmptyMessage("User", "profileImageUrl"));
	}

}
