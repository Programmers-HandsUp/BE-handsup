package com.core.domain.auction.auction_category;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ProductCategory {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "product_category_id")
	private Long id;

	@Column(name = "product_category_value")
	private ProductCategoryValue value;
}
