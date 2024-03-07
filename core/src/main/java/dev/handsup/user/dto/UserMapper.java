package dev.handsup.user.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.dto.response.UserReviewLabelResponse;
import dev.handsup.user.dto.response.UserReviewResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class UserMapper {

	public static User toUser(JoinUserRequest request, EncryptHelper encryptHelper) {
		String password = request.password();
		String encryptedPassword = encryptHelper.encrypt(password);
		Address address = Address.of(request.si(), request.gu(), request.dong());

		return User.of(
			request.email(),
			encryptedPassword,
			request.nickname(),
			address,
			request.profileImageUrl()
		);
	}

	public static UserReviewLabelResponse toUserReviewLabelResponse(UserReviewLabel userReviewLabel) {
		return UserReviewLabelResponse.of(
			userReviewLabel.getId(),
			userReviewLabel.getReviewLabel().getId(),
			userReviewLabel.getUser().getId(),
			userReviewLabel.getCount()
		);
	}

	public static UserReviewResponse toUserReviewResponse(Review review) {
		return UserReviewResponse.of(
			review.getWriter().getNickname(),
			review.getWriter().getProfileImageUrl(),
			review.getContent()
		);
	}
}
