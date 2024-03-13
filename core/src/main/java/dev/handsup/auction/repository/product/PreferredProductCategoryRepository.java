package dev.handsup.auction.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;
import dev.handsup.user.domain.User;

public interface PreferredProductCategoryRepository extends JpaRepository<PreferredProductCategory, Long> {
	List<PreferredProductCategory> findByUser(User user);
}
