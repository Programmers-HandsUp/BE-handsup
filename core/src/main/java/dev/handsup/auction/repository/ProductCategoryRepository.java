package dev.handsup.auction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auction.domain.product.product_category.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	Optional<ProductCategory> findByCategoryValue(String categoryValue);
}
