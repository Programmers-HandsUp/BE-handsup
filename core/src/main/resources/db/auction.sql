SET SESSION cte_max_recursion_depth = 500000;

INSERT INTO auction (
    seller_id, title, product_id, init_price, current_bidding_price, buy_price,
    end_date, trade_method, auction_status, bidding_count, bookmark_count, created_at, updated_at
)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 500000
)
SELECT
    FLOOR(1 + (RAND(n) * 10)), -- seller_id: 1~10 랜덤
    CASE
        WHEN MOD(n-1, 4) = 0 THEN '에어팟'
        WHEN MOD(n-1, 4) = 1 THEN '도서'
        WHEN MOD(n-1, 4) = 2 THEN '목걸이'
        ELSE '운동화'
        END AS title,
    n, -- product_id: 1~500000 일대일
    10000 + (FLOOR(RAND(n+20) * 90000)), -- init_price: 10,000~99,999
    10000 + (FLOOR(RAND(n+30) * 99000)), -- current_bidding_price
    CASE WHEN MOD(n, 5) = 0 THEN 20000 + FLOOR(RAND(n+40) * 90000) ELSE NULL END, -- buy_price
    DATE_ADD('2025-07-01', INTERVAL FLOOR(RAND(n+50)*180) DAY), -- end_date
    CASE WHEN MOD(n, 2) = 0 THEN 'DIRECT' ELSE 'DELIVER' END, -- trade_method (enum 주의)
    CASE MOD(n, 3) WHEN 0 THEN 'BIDDING' WHEN 1 THEN 'TRADING' ELSE 'COMPLETED' END, -- auction_status
    FLOOR(RAND(n+60)*21), -- bidding_count: 0~20
    FLOOR(RAND(n+70)*21), -- bookmark_count: 0~20
    NOW(),
    NOW()
FROM numbers;



UPDATE auction
SET
    si = '서울시',
    gu = CASE MOD(auction_id, 6)
             WHEN 0 THEN '관악구'
             WHEN 1 THEN '서초구'
             WHEN 2 THEN '관악구'
             WHEN 3 THEN '강남구'
             WHEN 4 THEN '송파구'
             ELSE '중구'
        END,
    dong = CASE MOD(auction_id, 6)
               WHEN 0 THEN '봉천동'
               WHEN 1 THEN '방배동'
               WHEN 2 THEN '신림동'
               WHEN 3 THEN '역삼동'
               WHEN 4 THEN '가락동'
               ELSE '무교동'
        END
WHERE auction_id BETWEEN 1 AND 500000;