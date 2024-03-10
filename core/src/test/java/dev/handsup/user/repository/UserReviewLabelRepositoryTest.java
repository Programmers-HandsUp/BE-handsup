package dev.handsup.user.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.fixture.UserFixture;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.ReviewLabelValue;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.user.domain.User;

@DisplayName("[UserReviewLabelRepository 테스트]")
class UserReviewLabelRepositoryTest extends DataJpaTestSupport {

	private final User user = UserFixture.user();
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserReviewLabelRepository userReviewLabelRepository;
	@Autowired
	private ReviewLabelRepository reviewLabelRepository;

	@Test
	@Transactional
	@DisplayName("[UserReviewLabel 엔티티의 count 필드를 update 할 수 있다.]")
	void testUpdateCount() {
		// given
		ReviewLabel reviewLabel = ReviewLabel.from(ReviewLabelValue.MANNER.getDescription());
		reviewLabelRepository.save(reviewLabel);

		UserReviewLabel userReviewLabel = UserReviewLabel.of(reviewLabel, user);
		int beforeUpdateCount = 5;
		ReflectionTestUtils.setField(userReviewLabel, "count", beforeUpdateCount);
		userReviewLabelRepository.save(userReviewLabel);

		// when
		int afterUpdateCount = 10;
		userReviewLabelRepository.updateCount(userReviewLabel.getId(), afterUpdateCount);
		entityManager.refresh(userReviewLabel);

		// then
		UserReviewLabel updatedUserReviewLabel = userReviewLabelRepository.findById(
			userReviewLabel.getId()).orElseThrow();

		assertThat(updatedUserReviewLabel.getCount()).isEqualTo(afterUpdateCount);
	}
}
