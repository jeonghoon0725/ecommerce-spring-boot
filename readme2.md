## 구현 요구사항

## **👤 1. 사용자 API (User-Facing)**

일반 사용자가 쇼핑몰 서비스를 이용하기 위해 직접 호출하는 공개 API입니다.

### **회원 가입**

- **Endpoint**: `POST /api/users`
- **설명**: 새로운 사용자의 정보를 시스템에 등록합니다.
- **요청 (Request Body)**:
    - `email` (String, 필수): 사용자 이메일. 로그인 ID로 사용되며, 시스템 내에서 **고유해야 합니다(Unique)**.
    - `password` (String, 필수): 사용자 비밀번호. 최소 8자 이상 등의 규칙을 적용할 수 있으며, 서버에서는 **반드시 암호화하여 저장**해야 합니다.
    - `username` (String, 필수): 사용자 이름 또는 닉네임.
- **기능 요구사항**:
    - 요청 시 `email`, `password`, `username` 필드는 비어있을 수 없습니다.
    - `email` 중복 확인 로직이 필요합니다. 이미 존재하는 이메일일 경우 에러를 반환해야 합니다.
- **응답 (Response)**:
    - **성공** : 가입된 사용자의 `id`와 `email`, `username`을 반환합니다.

        ```json
        {
          "result": true,
          "error": {},
          "message": {
            "id": 1,
            "email": "user@example.com",
            "username": "김철수"
          }
        }
        ```

    - **실패** : 필수 파라미터가 누락된 경우.

### **상품 목록 조회 (검색)**

- **Endpoint**: `GET /api/products`
- **설명**: 등록된 상품 목록을 다양한 조건으로 검색, 정렬, 페이징하여 조회합니다. **QueryDSL** 사용이 권장됩니다.
- **요청 (Query Parameters)**:
    - `category` (Long, 선택): 특정 카테고리 ID에 속한 상품만 필터링합니다.
    - `minPrice` (Integer, 선택): 최소 가격을 설정하여 해당 가격 이상의 상품만 필터링합니다.
    - `maxPrice` (Integer, 선택): 최대 가격을 설정하여 해당 가격 이하의 상품만 필터링합니다.
    - `page` (Integer, 선택, 기본값: 0): 조회할 페이지 번호 (0부터 시작).
    - `size` (Integer, 선택, 기본값: 10): 한 페이지에 보여줄 상품의 개수.
    - `sortBy` (String, 선택, 예: `price,asc` 또는 `createdAt,desc`): 정렬 기준. `(필드명),(정렬방식)` 형식으로 요청합니다.
- **기능 요구사항**:
    - 각 파라미터는 선택 사항이며, 조합하여 동적 쿼리를 생성해야 합니다.
    - `sortBy` 파라미터가 없으면 최신 등록순 (`createdAt,desc`)으로 기본 정렬합니다.
- **응답 (Response)**:
    - **성공** : 조회된 상품 목록과 함께 페이징 정보를 반환합니다.

        ```json
        {
          "result": true,
          "error": {},
          "message": {
            "content": [
              {
                "id": 10, "name": "스마트 워치", "price": 250000, "stock": 50 
              }
            ],
            "totalPages": 0,
            "totalElements": 0,
            "first": true,
            "last": true,
            "size": 0,
            "number": 0,
            "sort": {
              "empty": true,
              "unsorted": true,
              "sorted": true
            },
            "pageable": {
              "offset": 0,
              "sort": {
                "empty": true,
                "unsorted": true,
                "sorted": true
              },
              "unpaged": true,
              "paged": true,
              "pageNumber": 0,
              "pageSize": 0
            },
            "numberOfElements": 0,
            "empty": true
          }
          }
        }
        ```

### **상품 상세 조회**

- **Endpoint**: `GET /api/products/{productId}`
- **설명**: 특정 상품 ID를 사용하여 상품의 모든 상세 정보를 조회합니다.
- **요청 (Path Variable)**:
    - `productId` (Long, 필수): 조회할 상품의 고유 ID.
- **응답 (Response)**:
    - **성공** : 상품의 상세 정보를 반환합니다. 카테고리 정보도 함께 포함하면 좋습니다.

        ```json
        {
          "result": true,
          "error": {},
          "data": {
            "id": 10,
            "name": "스마트 워치",
            "description": "최신 기능이 탑재된 스마트 워치입니다.",
            "price": 250000,
            "stock": 50,
            "category": { "id": 5, "name": "전자기기" }
          }
        }
        ```

    - **실패** : 해당 `productId`의 상품이 존재하지 않을 경우.

### **카테고리 전체 계층 구조 조회**

- **Endpoint**: `GET /api/categories/hierarchy`
- **설명**: 시스템에 등록된 모든 카테고리를 부모-자식 관계가 명확한 계층(트리) 구조로 조회합니다. 웹사이트의 네비게이션 메뉴나 카테고리 필터를 동적으로 구성하는 데
  사용됩니다.
- **요청 (Request)**: 별도의 파라미터가 필요 없습니다.
- **기능 요구사항**:
    - 데이터베이스에서 모든 카테고리를 조회한 후, 서비스 로직에서 `parentId`를 이용해 트리 구조로 재조립해야 합니다.
    - 최상위 카테고리(`parentId`가 `null`인 카테고리)를 시작으로, 각 카테고리가 자신의 하위 카테고리 리스트를 `children` 필드로 갖는 형태여야 합니다.
    - N+1 쿼리 문제가 발생하지 않도록, 모든 카테고리를 한 번에 가져와 메모리에서 구조를 만드는 방식이 효율적입니다.
- **응답 (Response)**:
    - **성공** : 최상위 카테고리 목록을 포함하는 계층 구조의 JSON을 반환합니다.

        ```json
        {
          "result": true,
          "error": {},
          "message": [
            {
              "id": 1,
              "name": "의류",
              "children": [
                {
                  "id": 3,
                  "name": "상의",
                  "children": []
                },
                {
                  "id": 4,
                  "name": "하의",
                  "children": [
                    {
                      "id": 8,
                      "name": "청바지",
                      "children": []
                    }
                  ]
                }
              ]
            },
            {
              "id": 2,
              "name": "전자기기",
              "children": []
            }
          ]
        }
        ```

## **🛠️ 2. 어드민 API (Admin-Facing)**

관리자가 시스템의 데이터를 관리하기 위한 API입니다.

### **상품 등록**

- **Endpoint**: `POST /api/admin/products`
- **설명**: 관리자가 새로운 상품을 시스템에 등록합니다.
- **요청 (Request Body)**:
    - `name` (String, 필수): 상품명. 시스템 내에서 **고유해야 합니다(Unique)**.
    - `description` (String, 필수): 상품의 상세 설명.
    - `price` (Integer, 필수): 상품 가격. **0 이상의 정수**여야 합니다.
    - `stock` (Integer, 필수): 재고 수량. **0 이상의 정수**여야 합니다.
    - `categoryId` (Long, 필수): 상품이 속할 카테고리의 ID.
- **기능 요구사항**:
    - `price`와 `stock`은 음수일 수 없습니다.
    - `categoryId`에 해당하는 카테고리가 실제로 존재하는지 검증해야 합니다.
    - 상품명(`name`) 중복 검증 로직이 필요합니다.
- **응답 (Response)**:
    - **성공** : 생성된 상품의 ID를 반환합니다.

        ```json
        { "result": true, "message": { "productId": 11 }, "error": {} }
        ```

    - **실패** : 필수 필드가 누락되거나 유효성 검사(가격/재고가 음수)에 실패한 경우.
    - **실패** : 요청한 `categoryId`가 존재하지 않는 경우.

### **상품 수정**

- **Endpoint**: `PUT /api/admin/products/{productId}`
- **설명**: 기존 상품의 정보를 수정합니다.
- **요청 (Path Variable & Request Body)**:
    - `productId` (Long, 필수): 수정할 상품의 ID.
    - Request Body는 **상품 등록**과 동일한 필드 (`name`, `description`, `price`, `stock`, `categoryId`)를 가집니다.
- **응답 (Response)**:
    - **성공** : 수정이 완료된 상품의 상세 정보를 반환합니다.

        ```json
        {
          "result": true,
          "message": { "id": 11, "name": "업그레이드 워치", ... },
          "error": {}
        }
        ```

    - **실패** : 수정할 `productId` 또는 요청 Body의 `categoryId`가 존재하지 않을 경우.

### **상품 삭제**

- **Endpoint**: `DELETE /api/admin/products/{productId}`
- **설명**: 특정 상품을 시스템에서 삭제합니다.
- **요청 (Path Variable)**:
    - `productId` (Long, 필수): 삭제할 상품의 ID.
- **기능 요구사항**:
    - **(핵심 로직)** 삭제하려는 상품이 '주문 완료' 상태의 주문에 포함되어 있는지 확인해야 합니다.
    - `Purchase`와 `PurchaseProduct`테이블을 조인하여, 해당 `productId`가 `status`가 'COMPLETED'인 `Purchase`에 있는지
      검증하는 로직이 필요합니다.
    - 만약 포함되어 있다면 삭제를 거부하고 에러를 반환해야 합니다.
- **응답 (Response)**:
    - **성공** : 성공적으로 삭제되었으며, 별도의 Body는 없습니다.
    - **실패** : 삭제할 `productId`가 존재하지 않을 경우.

### **카테고리 등록**

- **Endpoint**: `POST /api/admin/categories`
- **설명**: 관리자가 새로운 카테고리를 등록합니다.
- **요청 (Request Body)**:
    - `name` (String, 필수): 카테고리명.
    - `description` (String, 선택): 카테고리 설명.
    - `parentId` (Long, 선택): 부모 카테고리의 ID. `null`로 보내면 최상위 카테고리로 생성됩니다.
- **응답 (Response)**:
    - **성공** : 생성된 카테고리의 ID를 반환합니다.
    - **실패** : `parentId`로 지정한 카테고리가 존재하지 않을 경우.

### **카테고리 수정**

- **Endpoint**: `PUT /api/admin/categories/{categoryId}`
- **설명**: 기존 카테고리의 정보를 수정합니다.
- **요청 (Path Variable & Request Body)**:
    - `categoryId` (Long, 필수): 수정할 카테고리의 ID.
    - Request Body: `name`, `description`, `parentId`.
- **기능 요구사항**:
    - 자기 자신을 부모로 지정하는 순환 참조가 발생하지 않도록 검증해야 합니다.
- **응답 (Response)**:
    - **성공** : 수정된 카테고리 정보를 반환합니다.
    - **실패** : 순환 참조를 시도할 경우.
    - **실패** : `categoryId` 또는 `parentId`가 존재하지 않을 경우.

### **카테고리 삭제**

- **Endpoint**: `DELETE /api/admin/categories/{categoryId}`
- **설명**: 특정 카테고리를 시스템에서 삭제합니다.
- **기능 요구사항**:
    - **조건 1**: 하위에 다른 카테고리가 없어야 합니다.
    - **조건 2**: 카테고리에 속한 상품이 없어야 합니다.
    - 두 조건을 모두 만족할 때만 삭제 가능하며, 아닐 경우 에러를 반환합니다.
- **응답 (Response)**:
    - **성공** : 성공적으로 삭제.
    - **실패** : `categoryId`가 존재하지 않을 경우.