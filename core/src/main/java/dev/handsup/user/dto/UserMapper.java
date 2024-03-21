package dev.handsup.user.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.dto.response.UserBuyHistoryResponse;
import dev.handsup.user.dto.response.UserReviewLabelResponse;
import dev.handsup.user.dto.response.UserReviewResponse;
import dev.handsup.user.dto.response.UserSaleHistoryResponse;
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
			userReviewLabel.getReviewLabel().getValue(),
			userReviewLabel.getCount()
		);
	}

	public static UserReviewResponse toUserReviewResponse(Review review) {
		return UserReviewResponse.of(
			review.getId(),
			review.getWriter().getNickname(),
			review.getWriter().getProfileImageUrl(),
			review.getContent()
		);
	}

	public static UserBuyHistoryResponse toUserBuyHistoryResponse(Auction auction) {
		return UserBuyHistoryResponse.of(
			auction.getId(),
			auction.getTitle(),
			auction.getProduct().getImages().get(0).getImageUrl(),
			auction.getCreatedAt().toString(),
			auction.getWinningPrice(),
			auction.getStatus().getLabel()
		);
	}

	public static UserSaleHistoryResponse toUserSaleHistoryResponse(Auction auction) {
		return UserSaleHistoryResponse.of(
			auction.getId(),
			auction.getTitle(),
			auction.getProduct().getImages().get(0).getImageUrl(),
			auction.getCreatedAt().toString(),
			auction.getCurrentBiddingPrice(),
			auction.getWinningPrice(),
			auction.getStatus().getLabel()
		);
	}
}
