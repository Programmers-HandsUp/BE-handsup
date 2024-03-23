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
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@Import(TestAuditingConfig.class)
class BiddingConcurrencyTest extends TestContainerSupport {

	private Auction auction;
	private User user;

	@Autowired
	private EntityManager em;

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

	// @DisplayName("[경매 필드 업데이트 요청을 동시에 500개 처리한다.]")
	// @Test
	// void concurrency_test() throws InterruptedException {
	// 	int threadCount = 500; // 요청 스레드 수
	// 	ExecutorService executorService = Executors.newFixedThreadPool(32);
	// 	CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 돕는 클래스
	//
	// 	// 여러 스레드에서 동시에 경매 필드값 업데이트 요청 수
	// 	for (int i = 0; i< threadCount; i++){
	// 		int finalI = i;
	// 		executorService.submit(() -> {
	// 			try {
	// 				biddingService.updateBiddingPriceAndCount(auction, 40000+finalI);
	// 				System.out.println("40000+finalI = " + 40000 + finalI);
	// 				System.out.println("auction.getCurrentBiddingPrice() = " + auction.getCurrentBiddingPrice());
	// 			} catch(Exception e){
	// 				throw new ValidationException(e.getMessage());
	// 			}
	// 			finally {
	// 				latch.countDown();
	// 			}
	// 		});
	// 	}
	//
	// 	latch.await(); //다른 스레드에서 수행중인 작업이 완료될 때까지 대기
	// 	assertEquals(500, auction.getBiddingCount());
	// 	assertEquals(auction.getCurrentBiddingPrice(), 40000+threadCount-1);
	// }

	@DisplayName("[동일한 경매 금액으로 입찰 시 하나의 입찰만 저장된다.]")
	@Test
	void concurrency_test2() throws InterruptedException {
		RegisterBiddingRequest request = RegisterBiddingRequest.from(auction.getInitPrice()+1000);
		int threadCount = 10; // 요청 스레드 수
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 돕는 클래스

		// 여러 스레드에서 동시에 경매 필드값 업데이트 요청 수
		for (int i = 0; i< threadCount; i++){
			executorService.submit(() -> {
				try {
					biddingService.registerBidding(request, auction.getId(), user);
					em.flush();
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
