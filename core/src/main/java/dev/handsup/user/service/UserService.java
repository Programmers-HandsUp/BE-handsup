package dev.handsup.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.product.PreferredProductCategoryRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.UserMapper;
import dev.handsup.user.dto.request.EmailAvailibilityRequest;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.dto.response.EmailAvailabilityResponse;
import dev.handsup.user.exception.UserErrorCode;
import dev.handsup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EncryptHelper encryptHelper;
	private final PreferredProductCategoryRepository preferredProductCategoryRepository;
	private final ProductCategoryRepository productCategoryRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_USER));
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_BY_EMAIL));
	}

	@Transactional
	public Long join(JoinUserRequest request) {
		validateDuplicateEmail(request.email());
		User user = UserMapper.toUser(request, encryptHelper);
		User savedUser = userRepository.save(user);    // 저장된 유저 확인

		request.productCategoryIds().forEach(productCategoryId -> {
			ProductCategory productCategory = getProductCategoryById(productCategoryId);
			preferredProductCategoryRepository.save(
				PreferredProductCategory.of(savedUser, productCategory)
			);
		});

		return savedUser.getId();
	}

	public EmailAvailabilityResponse isEmailAvailable(EmailAvailibilityRequest request) {
		boolean isEmailAvailable = userRepository.findByEmail(request.email()).isEmpty();
		return EmailAvailabilityResponse.from(isEmailAvailable);
	}

	private void validateDuplicateEmail(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new ValidationException(UserErrorCode.DUPLICATED_EMAIL);
		}
	}

	public ProductCategory getProductCategoryById(Long productCategoryId) {
		return productCategoryRepository.findById(productCategoryId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY));
	}
}
