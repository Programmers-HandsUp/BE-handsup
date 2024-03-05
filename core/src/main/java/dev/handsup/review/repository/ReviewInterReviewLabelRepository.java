package dev.handsup.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.review.domain.ReviewInterReviewLabel;

public interface ReviewInterReviewLabelRepository extends JpaRepository<ReviewInterReviewLabel, Long> {
}
