INSERT INTO products (name, description, price, stock, category_id, created_at, updated_at)
SELECT
    CONCAT('Product ', FLOOR(RAND() * 100000)),
    CONCAT('Description for product ', FLOOR(RAND() * 100000)),
    ROUND(RAND() * 1000, 2),
    FLOOR(RAND() * 100),
    FLOOR(RAND() * 1000),
    DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND() * 365) DAY),
    DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND() * 365) DAY)
FROM
    information_schema.tables t1
        CROSS JOIN information_schema.tables t2
    LIMIT 100000;