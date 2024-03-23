package dev.handsup.bidding.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.service.BiddingService;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.support.TestContainerSupport;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@SpringBootTest
@Transactional
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

	@BeforeEach
	void setUp(){
		ProductCategory productCategory = productCategoryRepository.save(ProductCategory.from("디지털 기기"));
		auction = auctionRepository.save(AuctionFixture.auction(productCategory));
		user = userRepository.save(UserFixture.user1());
	}

	@DisplayName("[경매 필드 업데이트 요청을 동시에 500개 처리한다.]")
	@Test
	void concurrency_test() throws InterruptedException {
		int threadCount = 500; // 요청 스레드 수
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 돕는 클래스

		// 여러 스레드에서 동시에 경매 필드값 업데이트 요청 수
		for (int i = 0; i< threadCount; i++){
			executorService.submit(() -> {
				try {
					biddingService.updateBiddingPriceAndCount(auction, 40000);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(); //다른 스레드에서 수행중인 작업이 완료될 때까지 대기
		assertEquals(500, auction.getBiddingCount());
	}
}
