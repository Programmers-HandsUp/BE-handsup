package dev.handsup.auction.domain.product;

import static dev.handsup.common.exception.CommonValidationError.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.common.entity.TimeBaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Product extends TimeBaseEntity {

	private static final String PRODUCT_STRING = "product";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Column(name = "status", nullable = false)
	@Enumerated(STRING)
	private ProductStatus status;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "purchase_time", nullable = false)
	@Enumerated(STRING)
	private PurchaseTime purchaseTime;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "product_category_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ProductCategory productCategory;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductImage> images = new ArrayList<>();

	@Builder
	private Product(
		ProductStatus status,
		String description,
		PurchaseTime purchaseTime,
		ProductCategory productCategory,
		List<String> imageUrls
	) {
		Assert.hasText(description, getNotEmptyMessage(PRODUCT_STRING, "description"));
		this.status = status;
		this.description = description;
		this.purchaseTime = purchaseTime;
		this.productCategory = productCategory;
		this.images.addAll(
			imageUrls.stream()
				.map(url -> new ProductImage(this, url))
				.toList()
		);
	}

	public static Product of(
		ProductStatus status,
		String description,
		PurchaseTime purchaseTime,
		ProductCategory productCategory,
		List<String> imageUrls
	) {
		return Product.builder()
			.status(status)
			.description(description)
			.purchaseTime(purchaseTime)
			.productCategory(productCategory)
			.imageUrls(imageUrls)
			.build();
	}
}
