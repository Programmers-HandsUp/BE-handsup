package dev.handsup.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
