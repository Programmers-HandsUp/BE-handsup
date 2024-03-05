package dev.handsup.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.handsup.review.domain.UserReviewLabel;

public interface UserReviewLabelRepository extends JpaRepository<UserReviewLabel, Long> {

	@Modifying
	@Query("update UserReviewLabel ur SET ur.count = :count where ur.id = :id")
	void updateCount(@Param("id") Long id, @Param("count") int count);
}
