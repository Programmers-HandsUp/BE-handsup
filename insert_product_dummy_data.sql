DELIMITER $$

DROP PROCEDURE IF EXISTS insert_product_dummy_data;
CREATE PROCEDURE insert_product_dummy_data()

BEGIN
    DECLARE i INT DEFAULT 0;
    WHILE i < 3000 DO
            INSERT INTO product (created_at, updated_at, description, purchase_time, status, product_category_id)
            VALUES (NOW(), NOW(), '애플 에어팟 맛집입니다^^', 'UNDER_ONE_MONTH', 'CLEAN', 1);
            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;
CALL insert_product_dummy_data();

# ALTER TABLE product AUTO_INCREMENT = 1;
# DELETE FROM product WHERE product_id >= 1;
