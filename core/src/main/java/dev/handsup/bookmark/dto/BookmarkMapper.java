package dev.handsup.bookmark.dto;

import static lombok.AccessLevel.*;

import org.springframework.data.domain.Slice;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BookmarkMapper {

	public static Bookmark toBookmark(User user, Auction auction) {
		return Bookmark.of(user, auction);
	}

	public static FindUserBookmarkResponse toFindUserBookmarkResponse(Auction auction) {
		return FindUserBookmarkResponse.of(
			auction.getId(),
			auction.getTitle(),
			auction.getStatus().getLabel(),
			auction.getCreatedAt().toLocalDate().toString(),
			null
		);
	}

	public static EditBookmarkResponse toEditBookmarkResponse(int bookmarkCount) {
		return new EditBookmarkResponse(bookmarkCount);
	}

	public static GetBookmarkStatusResponse toGetBookmarkStatusResponse(boolean isBookmarked) {
		return new GetBookmarkStatusResponse(isBookmarked);
	}

	public static <T> PageResponse<T> toPageResponse(Slice<T> page) {
		return PageResponse.of(page);
	}

}