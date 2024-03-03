package dev.handsup.user.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.fixture.UserFixture;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.ReviewLabelValue;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.support.DataJpaTestSupport;
import dev.handsup.user.domain.User;

@DisplayName("[UserReviewLabelRepository 테스트]")
class UserReviewLabelRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserReviewLabelRepository userReviewLabelRepository;

	private final User user = UserFixture.user();

	@Test
	@Transactional
	@DisplayName("[UserReviewLabel 엔티티의 count 필드를 update 할 수 있다.]")
	void testUpdateCount() {
		// given
		ReviewLabel reviewLabel = ReviewLabel.from(ReviewLabelValue.MANNER.getDescription());
		entityManager.persist(reviewLabel);
		UserReviewLabel userReviewLabel = UserReviewLabel.of(reviewLabel, user);
		int beforeUpdateCount = 5;
		ReflectionTestUtils.setField(userReviewLabel, "count", beforeUpdateCount);
		entityManager.persist(userReviewLabel);
		entityManager.flush();
		userReviewLabelRepository.save(userReviewLabel);

		// when
		int afterUpdateCount = 10;
		userReviewLabelRepository.updateCount(userReviewLabel.getId(), afterUpdateCount);

		UserReviewLabel refreshedUserReviewLabel = entityManager.find(UserReviewLabel.class, userReviewLabel.getId());
		entityManager.refresh(refreshedUserReviewLabel);

		// then
		Optional<UserReviewLabel> updatedUserReviewLabel = userReviewLabelRepository.findById(userReviewLabel.getId());
		assertThat(updatedUserReviewLabel.get().getCount()).isEqualTo(afterUpdateCount);
	}
}