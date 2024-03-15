package dev.handsup.user.service;

import static dev.handsup.user.exception.UserErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategoryValue;
import dev.handsup.auction.repository.product.PreferredProductCategoryRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[UserService 테스트]")
class UserServiceTest {

	private final User user = UserFixture.user();
	private final JoinUserRequest request = JoinUserRequest.of(
		user.getEmail(),
		user.getPassword(),
		user.getNickname(),
		user.getAddress().getSi(),
		user.getAddress().getGu(),
		user.getAddress().getDong(),
		user.getProfileImageUrl(),
		List.of(1L)
	);
	@Mock
	private UserRepository userRepository;
	@Mock
	private EncryptHelper encryptHelper;
	@Mock
	private ProductCategoryRepository productCategoryRepository;
	@Mock
	private PreferredProductCategoryRepository preferredProductCategoryRepository;
	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("[사용자 ID로 사용자를 성공적으로 조회한다]")
	void getUserByIdSuccessTest() {
		// given
		Long mockUserId = 1L;
		User mockUser = user;
		given(userRepository.findById(mockUserId))
			.willReturn(Optional.of(mockUser));

		// when
		User foundUser = userService.getUserById(mockUserId);

		// then
		assertThat(foundUser).isNotNull().isEqualTo(mockUser);
	}

	@Test
	@DisplayName("[사용자 ID로 사용자 조회에 실패한다 - 사용자 없음]")
	void getUserByIdFailTest() {
		// given
		Long userId = 1L;
		given(userRepository.findById(userId))
			.willReturn(Optional.empty());

		// when/then
		assertThatThrownBy(() -> userService.getUserById(userId))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	@MockitoSettings(strictness = Strictness.LENIENT)
	@DisplayName("[회원가입을 성공한다]")
	void joinSuccessTest() {
		// given
		given(userRepository.findByEmail(request.email()))
			.willReturn(Optional.empty());
		given(encryptHelper.encrypt(request.password()))
			.willReturn("encryptedPassword");

		User savedUser = UserFixture.user(1L);
		given(userRepository.save(any(User.class)))
			.willReturn(savedUser);

		ProductCategory productCategory = ProductCategory.from(ProductCategoryValue.SPORTS_LEISURE.getLabel());
		given(productCategoryRepository.findById(1L)).willReturn(Optional.of(productCategory));
		given(preferredProductCategoryRepository.save(PreferredProductCategory.of(savedUser, productCategory)))
			.willReturn(PreferredProductCategory.of(savedUser, productCategory));

		// when
		Long userId = userService.join(request);

		// then
		assertThat(userId).isNotNull().isEqualTo(1L);
		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("[회원가입을 실패한다 - 이메일 중복]")
	void joinFailTest() {
		// given
		given(userRepository.findByEmail(request.email()))
			.willReturn(Optional.of(user));

		// when & then
		assertThatThrownBy(() -> userService.join(request))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(DUPLICATED_EMAIL.getMessage());
		verify(userRepository, never()).save(any(User.class));
	}
}
