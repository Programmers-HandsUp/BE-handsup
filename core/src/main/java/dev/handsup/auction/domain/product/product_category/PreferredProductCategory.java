package dev.handsup.auction.domain.product.product_category;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.common.entity.TimeBaseEntity;
import dev.handsup.user.domain.User;
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
public class PreferredProductCategory extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "preferred_product_category_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "product_category_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ProductCategory productCategory;

	@Builder
	private PreferredProductCategory(User user, ProductCategory productCategory) {
		this.user = user;
		this.productCategory = productCategory;
	}

	public static PreferredProductCategory of(User user, ProductCategory productCategory) {
		return PreferredProductCategory.builder()
			.user(user)
			.productCategory(productCategory)
			.build();
	}
}
