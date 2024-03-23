package dev.handsup.bidding.repository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.service.BiddingService;
import dev.handsup.config.TestAuditingConfig;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.support.TestContainerSupport;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@Import(TestAuditingConfig.class)
class BiddingConcurrencyTest extends TestContainerSupport {

	private Auction auction;
	private User user;

	@Autowired
	private BiddingService biddingService;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BiddingRepository biddingRepository;

	@BeforeEach
	void setUp(){
		ProductCategory productCategory = productCategoryRepository.save(ProductCategory.from("디지털 기기"));
		auction = auctionRepository.save(AuctionFixture.auction(productCategory));
		user = userRepository.save(UserFixture.user1());
	}

	@DisplayName("[동일한 경매 금액으로 입찰 시 하나의 입찰만 저장된다.]")
	@Test
	void concurrency_test2() throws InterruptedException {
		RegisterBiddingRequest request = RegisterBiddingRequest.from(auction.getInitPrice()+1000);
		int threadCount = 30; // 요청 스레드 수
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 돕는 클래스

		for (int i = 0; i< threadCount; i++){
			executorService.submit(() -> {
				try {
					biddingService.registerBidding(request, auction.getId(), user);
				}
				catch (Exception e){
					log.info(e.getMessage());
				}
				finally {
					latch.countDown();
				}
			});
		}

		latch.await(); //다른 스레드에서 수행중인 작업이 완료될 때까지 대기
		Assertions.assertThat(biddingRepository.findAll()).hasSize(1);
	}
}
