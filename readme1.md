## 구현 요구사항

### 1단계: 장바구니(Shopping Cart) 도메인 모델링

'장바구니에 담은 상품을 구매'하는 기능을 지원하기 위한 데이터베이스 테이블과 JPA 엔티티를 설계합니다.

- **설계 목표**:
    - 장바구니와 사용자, 상품의 **N:N 관계**를 해결하기 위한 연결 테이블을 설계합니다.
- **설계 요구사항**:
    1. **테이블 설계 (Flyway)**: 아래 구조를 참고하여 `cart` 테이블 생성 SQL을 Flyway 마이그레이션 파일로 작성하세요.
        - `cart` 테이블: `id` (PK), `user_id` (FK to `user`), `product_id` (FK to `product`),
          `quantity`
    2. **엔티티 설계 (JPA)**: `Cart` 엔티티 클래스를 작성하고, `User`, `Product` 엔티티와의 연관관계를 어노테이션으로 매핑하세요.

### 2단계: 구매(Purchase) 및 환불(Refund) 도메인 확장 및 모델링

기존 구매 도메인에 **상태(Status)** 개념을 도입하고, **환불(Refund)**이라는 새로운 도메인을 설계하여 관계를 맺습니다.

- **설계 목표**:
    - 구매와 환불의 다양한 상태를 데이터 모델에 명확하게 표현합니다.
    - 구매와 환불의 관계(1:1)를 설계하고 매핑합니다.
- **설계 요구사항**:
    1. **상태 관리 설계**: `purchase` 테이블의 `status` 컬럼에 저장될 값들('PENDING', 'COMPLETED', 'CANCELED' 등)을 관리하기
       위한 **Java Enum(`PurchaseStatus`)**을 설계하세요. `refund` 테이블도 동일하게 `RefundStatus` Enum을 설계합니다.
    2. **테이블 설계 (Flyway)**: `refund` 테이블이 없다면 생성합니다. `purchase` 테이블과 1:1 관계를 맺기 위한 `purchase_id` 컬럼이
       포함되어야 합니다.
        - `refund` 테이블: `id` (PK), `purchase_id` (FK to `purchase`), `reason`, `status`
    3. **엔티티 설계 (JPA)**: `Refund` 엔티티를 작성하고, `Purchase` 엔티티와의 **`@ManyToOne`** 연관관계를 매핑하세요.

### 3단계: 최종 산출물

이 프로젝트의 최종 결과물은 다음과 같습니다.

1. **Flyway 마이그레이션 파일들**:
    - 총 8개(`user`, `category`, `product`, `purchase`, `purchase_product`, `cart`, `refund`) 테이블에 대한
      `CREATE TABLE` SQL 스크립트.
2. **완성된 JPA 엔티티 클래스들**:
    - 모든 테이블에 대한 Java 엔티티 클래스. 각 클래스는 필드, 컬럼 매핑, 그리고 **`@ManyToOne`, `@OneToMany` 등 모든
      연관관계가 `FetchType.LAZY`와 함께 완벽하게 설정**되어 있어야 합니다.
3. **데이터베이스 명세서:**
    - 생성한 테이블에 대한 데이터베이스 명세서를 아래 양식에 맞게 채워넣으면 됩니다.

### 💡 설계 시 핵심 고려사항

- **연관관계의 주인**: 1:N 관계에서 외래 키(FK)를 가진 N쪽이 주인이 되도록 `mappedBy` 속성을 정확히 사용하세요.
- **로딩 전략**: 모든 연관관계는 N+1 문제를 미리 방지하기 위해 **`FetchType.LAZY`**로 설정하는 것을 원칙으로 합니다.
- **네이밍 컨벤션**: 테이블명은 단수형, Java 필드는 `camelCase`, DB 컬럼은 `snake_case`로 프로젝트 전체의 일관성을 유지하세요.