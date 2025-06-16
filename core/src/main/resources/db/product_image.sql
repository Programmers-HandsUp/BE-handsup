INSERT INTO product_image (product_id, image_url)
SELECT product_id, 'https://hands-up-bucket.s3.ap-northeast-2.amazonaws.com/airpod.jpg'
FROM product
WHERE product_id BETWEEN 1 AND 500000;