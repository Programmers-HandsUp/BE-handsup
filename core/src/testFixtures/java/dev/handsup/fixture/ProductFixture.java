package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.product.product_category.ProductCategory;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ProductFixture {

	public static ProductCategory productCategory(String categoryValue){
		return ProductCategory.of(categoryValue);
	}
}
