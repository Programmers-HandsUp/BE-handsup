package dev.handsup.chat.controller;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.request.ChatMessageRequest;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.ChatMessageFixture;
import dev.handsup.fixture.ChatRoomFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

//포트 번호가 랜덤이 되고, @LocalServerPort로 해당 포트 번호 불러올 수 있음 -> 다른 테스트와 포트 충돌 방지
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketTest extends ApiTestSupport {

	@LocalServerPort
	private int port;
	private final User seller = UserFixture.user(); // loginUser
	private final User bidder = UserFixture.user("bidder@gmail.com");
	private BlockingQueue<ChatMessageResponse> chatMessageResponses;

	private StompSession stompSession;
	private String url;
	private ChatRoom chatRoom;

	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private BiddingRepository biddingRepository;
	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@BeforeEach
	void setUp() throws ExecutionException, InterruptedException, TimeoutException {
		url = "ws://localhost:" + port + "/ws-stomp";
		stompSession = getStompSession();

		chatMessageResponses = new LinkedBlockingDeque<>();

		ProductCategory productCategory = ProductFixture.productCategory("디지털 기기");
		productCategoryRepository.save(productCategory);

		userRepository.saveAll(List.of(seller, bidder));

		Auction auction = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction);

		Bidding bidding = BiddingFixture.bidding(auction, bidder);
		biddingRepository.save(bidding);

		chatRoom = ChatRoomFixture.chatRoom(auction.getId(), seller, bidding.getBidder());
		chatRoomRepository.save(chatRoom);


	}

	@DisplayName("[채팅방에서 메시지 전송 API 호출할 수 있다.]")
	@Test
	void ChatMessage() throws InterruptedException {
		// given
		ChatMessage chatMessage = ChatMessageFixture.chatMessage(chatRoom, seller);
		ChatMessageRequest request = ChatMessageRequest.of(chatMessage.getSenderId(), chatMessage.getContent());

		stompSession.subscribe("/subscribe/chat-rooms/" + chatRoom.getId(),
			new StompFrameHandlerImpl<>(ChatMessageResponse.class, chatMessageResponses));

		ChatMessageResponse expected = ChatMessageResponse.from(chatMessage);

		// when
		stompSession.send("/publish/chat-rooms/" + chatRoom.getId(), request);
		ChatMessageResponse result = chatMessageResponses.poll(5, TimeUnit.SECONDS); // 큐에 저장된 요소 하나 꺼냄

		// then
		assertThat(result).usingRecursiveComparison()
			.ignoringFields("createdAt")
			.isEqualTo(expected);
	}

	private StompSession getStompSession() throws ExecutionException, InterruptedException, TimeoutException {
		// 웹소켓 연결하는 클라이언트 생성
		StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();

		// 웹 소켓 연결 처리
		WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
		SockJsClient sockJsClient = new SockJsClient(List.of(webSocketTransport)); // sockJs 클라이언트 생성
		WebSocketStompClient webSocketStompClient = new WebSocketStompClient(sockJsClient); // 웹소켓 클라이언트 생성

		// webSocketStompClient에 역직렬화를 위한 converter 지정
		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
		ObjectMapper objectMapper = messageConverter.getObjectMapper();
		objectMapper.registerModules(new JavaTimeModule()); // javaTimeModule -> LocalDateTime 역직렬화
		webSocketStompClient.setMessageConverter(messageConverter);

		// 비동기 연결 시도 -> 연결 성공 시 StompSession 객체 반환
		return webSocketStompClient
			.connectAsync(url, new StompSessionHandlerAdapter() {})
			.get(2, TimeUnit.SECONDS);
	}

}

class StompFrameHandlerImpl<T> implements StompFrameHandler {

	private final Type responseType; // 메시지의 응답 타입 저장
	private final BlockingQueue<T> responses; // 메시지 처리 결과 저장

	// 응답 타입과 응답을 저장할 큐를 인자로 받아 인스턴스 변수에 할당
	public StompFrameHandlerImpl(final Class<T> responseType, final BlockingQueue<T> responses) {
		this.responseType = responseType;
		this.responses = responses;
	}

	@Override
	public Type getPayloadType(final StompHeaders headers) {
		return responseType;
	}

	@Override
	public void handleFrame(final StompHeaders headers, final Object payload) {
		responses.offer((T)payload);
	}
}
