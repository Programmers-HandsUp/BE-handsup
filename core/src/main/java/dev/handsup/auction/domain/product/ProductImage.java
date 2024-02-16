package dev.handsup.auction.domain.product;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ProductImage extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "product_image_id")
	private Long id;

	@Column(name = "image_url")
	private String imageUrl;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Product product;

	@Builder
	public ProductImage(String imageUrl, Product product) {
		this.imageUrl = imageUrl;
		this.product = product;
	}
}
