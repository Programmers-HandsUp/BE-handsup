package dev.handsup.chat.repository;

import org.junit.jupiter.api.DisplayName;

import dev.handsup.support.DataJpaTestSupport;

@DisplayName("[ChatRoomRepository 테스트]")
class ChatRoomRepositoryTest extends DataJpaTestSupport {
	// private final PageRequest pageRequest = PageRequest.of(0, 10);
	// private User user1, user2, user3;
	// private Auction auction1, auction2;
	// @Autowired
	// private ChatRoomRepository chatRoomRepository;
	// @Autowired
	// private UserRepository userRepository;
	// @Autowired
	// private AuctionRepository auctionRepository;
	// @Autowired
	// private ProductCategoryRepository productCategoryRepository;
	//
	// @BeforeEach
	// void setUp() {
	// 	ProductCategory category = ProductFixture.productCategory("디지털 기기");
	// 	productCategoryRepository.save(category);
	//
	// 	user1 = UserFixture.user("user1@gmail.com");
	// 	user2 = UserFixture.user("user2@gmail.com");
	// 	user3 = UserFixture.user("user3@gmail.com");
	// 	userRepository.saveAll(List.of(user1, user2, user3));
	//
	// 	auction1 = AuctionFixture.auction(category);
	// 	auction2 = AuctionFixture.auction(category);
	//
	// 	auctionRepository.saveAll(List.of(auction1, auction2));
	// }
	//
	// @DisplayName("[유저가 속한 채팅방을 모두 조회할 수 있다.]")
	// @Test
	// void findChatRoomsByUser() {
	// 	//given
	// 	ChatRoom chatRoom1 = ChatRoomFixture.chatRoom(auction1.getId(), user1, user2);
	// 	ChatRoom chatRoom2 = ChatRoomFixture.chatRoom(auction2.getId(), user2, user1);
	// 	ChatRoom chatRoom3 = ChatRoomFixture.chatRoom(auction2.getId(), user1, user3);
	// 	ChatRoom chatRoomWithoutUser1 = ChatRoomFixture.chatRoom(auction2.getId(), user2, user3);
	// 	chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2, chatRoom3, chatRoomWithoutUser1));
	// 	//when
	// 	Slice<ChatRoom> chatRoomSlice = chatRoomRepository.findChatRoomsByUser(user1, pageRequest);
	// 	List<ChatRoom> chatRooms = chatRoomSlice.getContent();
	//
	// 	//then
	// 	assertAll(
	// 		() -> assertThat(chatRoomSlice).hasSize(3),
	// 		() -> assertThat(chatRoomSlice.hasNext()).isFalse(),
	// 		() -> assertThat(chatRooms.get(0)).isEqualTo(chatRoom1),
	// 		() -> assertThat(chatRooms.get(1)).isEqualTo(chatRoom2),
	// 		() -> assertThat(chatRooms.get(2)).isEqualTo(chatRoom3)
	// 	);
	// }
}