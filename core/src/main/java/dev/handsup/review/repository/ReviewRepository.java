package dev.handsup.review.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Slice<Review> findByAuctionIdOrderByCreatedAtDesc(Long auctionId, Pageable pageable);
}
