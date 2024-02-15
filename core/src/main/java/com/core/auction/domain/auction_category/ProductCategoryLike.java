package com.core.auction.domain.auction_category;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.core.common.entity.TimeBaseEntity;
import com.core.user.domain.User;

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
public class ProductCategoryLike extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "product_category_like_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "product_category_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ProductCategory productCategory;

	@Builder
	public ProductCategoryLike(User user, ProductCategory productCategory) {
		this.user = user;
		this.productCategory = productCategory;
	}
}
