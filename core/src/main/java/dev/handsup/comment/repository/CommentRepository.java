package dev.handsup.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auction.domain.Auction;
import dev.handsup.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	Slice<Comment> findByAuctionOrderByCreatedAtDesc(Auction auction, Pageable pageable);
}
