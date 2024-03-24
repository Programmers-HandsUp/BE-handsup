DELIMITER $$

DROP PROCEDURE IF EXISTS insert_auction_dummy_data;
CREATE PROCEDURE insert_auction_dummy_data()

BEGIN
    DECLARE i INT DEFAULT 0;
    WHILE i < 3000 DO
            INSERT INTO auction
            (created_at, updated_at, bidding_count, bookmark_count, current_bidding_price, end_date, init_price, auction_status, title, trade_method, dong, gu, si, buyer_id, product_id, seller_id, buy_price)
            VALUES
                (NOW(), NOW(), FLOOR(RAND() * 10), FLOOR(RAND() * 10), 3000, '2024-04-07', 3000, 'BIDDING', '에어팟 거의 새거', 'DIRECT', '동선동', '성북구', '서울시', NULL, 1 + i, 2, NULL);
            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

CALL insert_auction_dummy_data();

# ALTER TABLE auction AUTO_INCREMENT = 1;
# DELETE FROM auction WHERE auction_id >= 1;
