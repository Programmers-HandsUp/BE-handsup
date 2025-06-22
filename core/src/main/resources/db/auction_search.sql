INSERT INTO auction_search (
    auction_id,
    product_id,
    category,
    title,
    img_url,
    end_date,
    si, gu, dong,
    trade_method,
    current_bidding_price,
    bidding_count,
    bookmark_count,
    is_new_product,
    is_progress,
    created_at
)
SELECT
    a.auction_id,
    a.product_id,
    pc.value AS category,
    a.title,
    pi.image_url AS img_url,
    a.end_date,
    a.si,
    a.gu,
    a.dong,
    a.trade_method,
    a.current_bidding_price,
    a.bidding_count,
    a.bookmark_count,
    CASE
        WHEN p.status = 'NEW' THEN 1
        ELSE 0
        END AS is_new_product,
    true,
    a.created_at
FROM auction a
         JOIN product p ON a.product_id = p.product_id
         JOIN product_category pc ON p.product_category_id = pc.product_category_id
         LEFT JOIN (
    SELECT product_id, MIN(image_url) AS image_url
    FROM product_image
    GROUP BY product_id
) pi ON p.product_id = pi.product_id;



UPDATE auction_search
SET category = CONCAT(
        '카테고리_',
        LPAD(
                MOD(auction_search_id - 1, 100000) + 1,
                6, '0'
        )
               )
WHERE auction_search_id >= 1;


UPDATE auction_search
SET category =
        CASE
            WHEN MOD(auction_search_id - 1, 13) = 0 THEN '디지털 기기'
            WHEN MOD(auction_search_id - 1, 13) = 1 THEN '가구/인테리어'
            WHEN MOD(auction_search_id - 1, 13) = 2 THEN '패션/잡화'
            WHEN MOD(auction_search_id - 1, 13) = 3 THEN '생활가전'
            WHEN MOD(auction_search_id - 1, 13) = 4 THEN '생활/주방'
            WHEN MOD(auction_search_id - 1, 13) = 5 THEN '스포츠/레저'
            WHEN MOD(auction_search_id - 1, 13) = 6 THEN '취미/게임/음반'
            WHEN MOD(auction_search_id - 1, 13) = 7 THEN '뷰티/미용'
            WHEN MOD(auction_search_id - 1, 13) = 8 THEN '반려동물용품'
            WHEN MOD(auction_search_id - 1, 13) = 9 THEN '티켓/교환권'
            WHEN MOD(auction_search_id - 1, 13) = 10 THEN '도서'
            WHEN MOD(auction_search_id - 1, 13) = 11 THEN '유아도서'
            ELSE '기타중고물품'
            END
where auction_search_id >= 1;


UPDATE auction_search
SET bookmark_count = FLOOR(RAND() * 1000)
where auction_search_id >= 1;