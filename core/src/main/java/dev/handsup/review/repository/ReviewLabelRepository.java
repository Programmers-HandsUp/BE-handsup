package dev.handsup.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.review.domain.ReviewLabel;

public interface ReviewLabelRepository extends JpaRepository<ReviewLabel, Long> {
}
