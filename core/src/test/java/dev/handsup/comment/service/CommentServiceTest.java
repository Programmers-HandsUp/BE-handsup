package dev.handsup.comment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import dev.handsup.user.repository.UserRepository;

@DisplayName("[댓글 서비스 테스트]")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@Mock
	private AuctionRepository auctionRepository;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CommentService commentService;

	@BeforeEach
	void setUp() {

	}

	@DisplayName("[채팅을 등록할 수 있다.]")
	@Test
	void registerComment() {
		//given
		RegisterCommentRequest request = new RegisterCommentRequest("와");
		Auction auction = AuctionFixture.auction();
		User writer = UserFixture.user2();
		Comment comment = CommentFixture.comment(auction, writer);

		given(auctionRepository.findById(auction.getId())).willReturn(Optional.of(auction));
		given(commentRepository.save(any(Comment.class))).willReturn(comment);

		//when
		CommentResponse response = commentService.registerAuctionComment(auction.getId(), request, writer);

		//then
		assertThat(response).isNotNull();
	}
}