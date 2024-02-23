package dev.handsup.bookmark.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.bookmark.dto.EditBookmarkResponse;
import dev.handsup.bookmark.dto.FindUserBookmarkResponse;
import dev.handsup.bookmark.dto.GetBookmarkStatusResponse;
import dev.handsup.bookmark.repository.BookmarkRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BookmarkFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {
	private final User user = UserFixture.user();
	private final PageRequest pageRequest = PageRequest.of(0, 5);
	@Mock
	private AuctionRepository auctionRepository;
	@Mock
	private BookmarkRepository bookmarkRepository;
	@InjectMocks
	private BookmarkService bookmarkService;

	private final Auction auction = AuctionFixture.auction();

	@DisplayName("[북마크를 추가할 수 있다.]")
	@Test
	void addBookmark() {
		//given
		int initialBookmarkCount = auction.getBookmarkCount();
		Bookmark bookmark = BookmarkFixture.bookmark(user, auction);

		given(auctionRepository.findById(auction.getId())).willReturn(Optional.of(auction));
		given(bookmarkRepository.findByUserAndAuction(user, auction)).willReturn(Optional.empty());
		given(bookmarkRepository.save(any(Bookmark.class))).willReturn(bookmark);

		//when
		EditBookmarkResponse response = bookmarkService.addBookmark(user, auction.getId());

		//then
		assertThat(response.bookmarkCount()).isEqualTo(initialBookmarkCount + 1);
	}

	@DisplayName("[북마크를 삭제할 수 있다.]")
	@Test
	void cancelBookmark() {
		//given
		int initialBookmarkCount = auction.getBookmarkCount();
		Bookmark bookmark = BookmarkFixture.bookmark(user, auction);

		given(auctionRepository.findById(auction.getId())).willReturn(Optional.of(auction));
		given(bookmarkRepository.findByUserAndAuction(user, auction)).willReturn(Optional.of(bookmark));

		//when
		EditBookmarkResponse response = bookmarkService.cancelBookmark(user, auction.getId());

		//then
		assertThat(response.bookmarkCount()).isEqualTo(initialBookmarkCount - 1);
	}

	@DisplayName("[유저 북마크 여부를 확인할 수 있다.")
	@Test
	void checkBookmarkStatus() {
		//given
		given(auctionRepository.findById(auction.getId())).willReturn(Optional.of(auction));
		given(bookmarkRepository.existsByUserAndAuction(user, auction)).willReturn(true);

		//when
		GetBookmarkStatusResponse response = bookmarkService.getBookmarkStatus(user, auction.getId());

		//then
		assertThat(response.isBookmarked()).isTrue();
	}

	@DisplayName("[유저 북마크를 모두 조회할 수 있다.]")
	@Test
	void findUserBookmarks() {
		//given
		given(auctionRepository.findBookmarkAuction(user, pageRequest))
			.willReturn(new SliceImpl<>(List.of(auction), pageRequest, false));
		ReflectionTestUtils.setField(auction, "createdAt", LocalDateTime.now());

		//when
		PageResponse<FindUserBookmarkResponse> response
			= bookmarkService.findUserBookmarks(user, pageRequest);

		//then
		assertThat(response.size()).isEqualTo(1);
		assertThat(response.content().get(0).createdDate())
			.isEqualTo(auction.getCreatedAt().toLocalDate().toString());
	}
}
