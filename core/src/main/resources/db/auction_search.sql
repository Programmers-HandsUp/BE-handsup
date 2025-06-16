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