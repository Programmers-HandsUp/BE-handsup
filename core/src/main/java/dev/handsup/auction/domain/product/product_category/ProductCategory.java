package dev.handsup.auction.domain.product.product_category;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
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

	@Column(name = "value", nullable = false)
	private String value;

	@Builder
	private ProductCategory(String value) {
		this.value = value;
	}

	public static ProductCategory of(String value) {
		return ProductCategory.builder()
			.value(value)
			.build();
	}
}
