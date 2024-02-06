package com.example.handsup.domain.user;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.example.handsup.common.TimeBaseEntity;

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
	@Column(name = "member_id")
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
	public User(String email, String password, String nickname, int score, Address address, String profileImageUrl) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.score = score;
		this.address = address;
		this.profileImageUrl = profileImageUrl;
		this.reportCount = 0;
	}
}
