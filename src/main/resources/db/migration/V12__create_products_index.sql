/*
SELECT *
FROM products
ORDER BY id ASC
    LIMIT 10 OFFSET 20; -- 20개를 건너뛰고 10개를 가져옴

- 10만 개를 건너뛰고 10개를 가져오는 쿼리
SELECT *
FROM products
ORDER BY created_at DESC
    LIMIT 10 OFFSET 99990;

- 첫 페이지 요청
SELECT id, name, created_at
FROM products
ORDER BY created_at DESC, id DESC
    LIMIT 10;
-- (결과로 마지막 데이터가 id=150, created_at='2025-07-15 10:00:00' 이었다고 가정)

-- 다음 페이지 요청
SELECT id, name, created_at
FROM products
WHERE (created_at, id) < ('2023-12-31 00:00:00', 97543) -- 튜플 비교로 시작점 지정
ORDER BY created_at DESC, id DESC
    LIMIT 10;


- 최적화 대상 쿼리
EXPLAIN
SELECT id, name, price, created_at
FROM products
ORDER BY created_at DESC, id DESC
    LIMIT 10 OFFSET 10000;
*/
-- ✅ 커버링 인덱스 생성
CREATE INDEX idx_products_created_id ON products (created_at, id);
/*
-- ✅ 커버링 인덱스를 활용한 최적화 쿼리
EXPLAIN
SELECT p.id, p.name, p.price, p.created_at
FROM products p
         INNER JOIN (
    -- 1단계: id를 2차 정렬 조건으로 추가하여 항상 동일한 id 목록을 보장
    SELECT id
    FROM products
    ORDER BY created_at DESC, id DESC
        LIMIT 10 OFFSET 10000
) AS page ON p.id = page.id
-- 2단계: 최종 결과의 정렬 순서도 동일하게 맞춰준다.
ORDER BY p.created_at DESC, p.id DESC;


- ❌ 비효율적인 쿼리
EXPLAIN
SELECT * FROM products LIMIT 10;

-- ✅ 최적화된 쿼리
SELECT id, name, price FROM products LIMIT 10;


-- 10만 번째 페이지 근처의 데이터 조회
EXPLAIN
SELECT id, name, price
FROM products
ORDER BY created_at DESC
    LIMIT 10 OFFSET 100000;


-- 첫 페이지 요청
SELECT id, name, created_at
FROM products
ORDER BY created_at DESC, id DESC -- 2차 정렬 조건 추가
    LIMIT 10;
-- 결과: 마지막 행이 id=97543, created_at='2023-12-31 00:00:00' 이라 가정

-- 다음 페이지 요청 (애플리케이션은 마지막 행의 값을 기억해야 함)
SELECT id, name, created_at
FROM products
WHERE (created_at, id) < ('2023-12-31 00:00:00', 97543) -- 튜플 비교
ORDER BY created_at DESC, id DESC
    LIMIT 10;

 */