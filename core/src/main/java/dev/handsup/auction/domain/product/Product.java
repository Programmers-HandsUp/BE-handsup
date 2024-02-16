package dev.handsup.auction.domain.product;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.auction_field.ProductStatus;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Product extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Column(name = "status")
	@Enumerated(STRING)
	private ProductStatus status;

	@Column(name = "description")
	private String description;

	@Column(name = "purchase_time")
	@Enumerated(STRING)
	private PurchaseTime purchaseTime;


	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "product_category_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ProductCategory category;

	@Builder
	public Product(ProductStatus status, String description, PurchaseTime purchaseTime, ProductCategory category) {
		this.status = status;
		this.description = description;
		this.purchaseTime = purchaseTime;
		this.category = category;
	}
}