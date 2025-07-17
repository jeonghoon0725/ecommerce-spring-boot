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
/*
**EXPLAIN
    id      쿼리 내의 SELECT 문 실행 순서. 숫자가 높을수록 먼저 실행
    *type	(가장 중요) 데이터 접근 방식. 성능 핵심 지표
    key	    실제로 사용된 인덱스의 이름. NULL이면 인덱스를 사용 X
    rows	쿼리를 실행하기 위해 스캔할 것으로 예측되는 행의 수
    *Extra	쿼리 실행에 대한 추가 정보. Using filesort, Using temporary 등 발생 시 주의

*type
    ALL     FULL SCAN
    index   INDEX FULL SCAN
    range   INDEX RANGE SCAN
    ref     인덱스를 사용한 조인 동등 비교
    eq_ref  PK or FK 조인 동등 비교
    const   PK or Unique 행 검색
    system  테이블에 행 단 하나

*Extra
    Using where      WHERE 절을 통해 데이터를 필터링했음을 의미 / type=ALL, Using where가 있다면 전체 데이터를 읽고 필터링하는 비효율적인 상황
    Using filesort   ORDER BY 구문을 처리하기 위해 인덱스를 사용하지 못하고, 별도의 정렬 작업을 수행했다는 의미
    Using temporary  GROUP BY 나 JOIN 등을 처리하기 위해 임시 테이블을 생성했다는 의미

 */