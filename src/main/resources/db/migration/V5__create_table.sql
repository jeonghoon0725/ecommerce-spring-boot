-- purchase 테이블
CREATE TABLE purchase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, -- FK: 어떤 user의 주문인지 식별
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)
);


-- product Table
CREATE TABLE product (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     description TEXT,
     price DECIMAL(10, 2) NOT NULL,
     stock INT NOT NULL DEFAULT 0,
     category_id BIGINT COMMENT '상품이 속한 카테고리 ID',
     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 연결 테이블: `purchase_item`
CREATE TABLE purchase_product (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   purchase_id BIGINT NOT NULL,   -- FK: 어떤 주문에 속하는지
   product_id BIGINT NOT NULL,    -- FK: 어떤 상품인지
   quantity INT NOT NULL,         -- 주문 수량
   price DECIMAL(10, 2) NOT NULL,  -- 주문 시점의 상품 가격
   created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT DEFAULT NULL, -- FK: 부모 카테고리의 id를 참조
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

