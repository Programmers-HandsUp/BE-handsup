package dev.handsup.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auction.domain.Auction;
import dev.handsup.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByAuction(Auction auction);
}
