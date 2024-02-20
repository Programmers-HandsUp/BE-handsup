package dev.handsup.auction.repository.auction;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.support.DataJpaTestSupport;

class AuctionQueryRepositoryImplTest extends DataJpaTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private final String APPLIANCE = "가전제품";
	private final int PAGE_NUMBER = 0;
	private final int PAGE_SIZE = 10;
	private ProductCategory category1;
	private ProductCategory category2;
	@Autowired
	private AuctionQueryRepository auctionQueryRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		category1 = ProductFixture.productCategory(DIGITAL_DEVICE);
		category2 = ProductFixture.productCategory(APPLIANCE);
		productCategoryRepository.saveAll(List.of(category1, category2));
	}

	@DisplayName("경매 카테고리로 필터링할 수 있다.")
	@Test
	void category_filter() {
		//given
		Auction auction1 = AuctionFixture.auction(category1);
		Auction auction2 = AuctionFixture.auction(category2);
		assertThat(auction1.getProduct().getProductCategory().getCategoryValue()).isEqualTo(DIGITAL_DEVICE);
		auctionRepository.saveAll(List.of(auction1, auction2));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.productCategory(DIGITAL_DEVICE)
			.build();

		PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

		//when
		List<Auction> auctions = auctionQueryRepository.findAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctions).hasSize(1),
			() -> assertThat(auctions.get(0).getProduct().getProductCategory().getCategoryValue())
				.isEqualTo(DIGITAL_DEVICE)
		);
	}

}