SET SESSION cte_max_recursion_depth = 200000;

INSERT INTO bookmark (user_id, auction_id, created_at)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 100000
)
SELECT
    FLOOR(1 + RAND(n) * 10)            AS user_id,
    FLOOR(1 + RAND(n + 1234) * 500000) AS auction_id,
    NOW()                              AS created_at
FROM numbers;



