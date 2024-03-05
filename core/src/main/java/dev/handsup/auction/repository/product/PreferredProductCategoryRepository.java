package dev.handsup.auction.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;

public interface PreferredProductCategoryRepository extends JpaRepository<PreferredProductCategory, Long> {
}
