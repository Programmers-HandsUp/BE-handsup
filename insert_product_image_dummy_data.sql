DELIMITER $$

DROP PROCEDURE IF EXISTS insert_product_image_dummy_data;
CREATE PROCEDURE insert_product_image_dummy_data()

BEGIN
    DECLARE i INT DEFAULT 0;
    WHILE i < 3000 DO
            INSERT INTO product_image (created_at, updated_at, image_url, product_id)
            VALUES (NOW(), NOW(), "image_url_string", i + 1);
            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;
CALL insert_product_image_dummy_data();

# ALTER TABLE product_image AUTO_INCREMENT = 1;
# DELETE FROM product_image WHERE product_image_id >= 1;
