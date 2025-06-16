use handsup_local;
-- product 테이블에 50만건 더미 데이터 생성
SET SESSION cte_max_recursion_depth = 500000;

INSERT INTO product (status, description, purchase_time, product_category_id, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 500000
)
SELECT
    CASE MOD(n, 3)
        WHEN 0 THEN 'NEW'
        WHEN 1 THEN 'CLEAN'
        ELSE 'DIRTY'
        END,
    CONCAT('샘플 설명 ', n),
    CASE MOD(n, 6)
        WHEN 0 THEN 'UNDER_ONE_MONTH'
        WHEN 1 THEN 'UNDER_THREE_MONTH'
        WHEN 2 THEN 'UNDER_SIX_MONTH'
        WHEN 3 THEN 'UNDER_ONE_YEAR'
        WHEN 4 THEN 'ABOVE_ONE_YEAR'
        ELSE 'UNKNOWN'
        END,
    FLOOR(1 + (RAND(n) * 13)),
    NOW(),
    NOW()
FROM numbers;

UPDATE product
SET product_category_id =
        CASE
            WHEN MOD(product_id-1, 4) = 0 THEN 1    -- 에어팟
            WHEN MOD(product_id-1, 4) = 1 THEN 11   -- 도서
            WHEN MOD(product_id-1, 4) = 2 THEN 3    -- 목걸이
            ELSE 3                          -- 운동화
            END
WHERE product_id BETWEEN 1 AND 500000;
