package dev.handsup.comment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import dev.handsup.comment.domain.Comment;
import dev.handsup.comment.dto.request.RegisterCommentRequest;
import dev.handsup.comment.dto.response.CommentResponse;
import dev.handsup.comment.repository.CommentRepository;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.CommentFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@DisplayName("[댓글 서비스 테스트]")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	private Auction auction;
	private User writer;
	@Mock
	private AuctionRepository auctionRepository;

	@Mock
	private CommentRepository commentRepository;

	@InjectMocks
	private CommentService commentService;

	@BeforeEach
	void setUp() {
		auction = AuctionFixture.auction();
		writer = UserFixture.user2();
	}

	@DisplayName("[댓글을 등록할 수 있다.]")
	@Test
	void registerComment() {
		//given
		RegisterCommentRequest request = new RegisterCommentRequest("와");

		Comment comment = CommentFixture.comment(auction, writer);
		ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.now());

		given(auctionRepository.findById(auction.getId())).willReturn(Optional.of(auction));
		given(commentRepository.save(any(Comment.class))).willReturn(comment);

		//when
		CommentResponse response = commentService.registerAuctionComment(auction.getId(), request, writer);

		//then
		assertThat(response).isNotNull();
	}

	@DisplayName("[한 경매에 대한 댓글을 모두 조회할 수 있다.]")
	@Test
	void getAuctionComments() {
		//given
		PageRequest pageRequest = PageRequest.of(0, 5);
		Comment comment = CommentFixture.comment(auction, writer);
		ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.now());

		given(auctionRepository.findById(auction.getId()))
			.willReturn(Optional.of(auction));
		given(commentRepository.findByAuctionOrderByCreatedAtDesc(auction, pageRequest))
			.willReturn(new SliceImpl<>(List.of(comment), pageRequest, false));

		//when
		List<CommentResponse> content = commentService.getAuctionComments(auction.getId(), pageRequest).content();

		//then
		assertThat(content).hasSize(1);
		assertThat(content.get(0).writerId()).isEqualTo(comment.getWriter().getId());
	}
}