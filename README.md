# jwp-shopping-cart

## 1단계 기능 요구사항

### 상품 목록 페이지 연동

- [x] 상품은 아래와 같은 필드를 가지고 있다.
    - ID(long)
    - Name(String)
    - Image(String)
    - Price(int)

- [x] 컨트롤러는 아래와 같은 요청을 처리한다.
    - [x] '/' 경로로 get 요청이 들어올 경우, 상품 목록 페이지를 반환한다.

- [x] 상품서비스는 아래와 같은 기능을 한다.
    - [x] 전체 상품 목록을 DB에서 가져온다.
    - [x] 전체 상품 목록을 컨트롤러에 보낸다.

### 상품 관리 CRUD API 작성

- [x] 상품 생성
    - '/admin/product/create' 경로로 post 요청이 들어올 경우, HttpStatus.CREATED 반환한다.
        - [x] Controller에서는 RequestParam으로 받은 데이터를 DTO로 변환하여 Service에게 전달
        - [x] Service에서는 DTO를 Product로 변환해서 DAO에 전달
        - [x] DAO는 데이터베이스에 Product INSERT
- [x] 상품 목록 조회
    - '/admin/product/list' 경로로 get 요청이 들어올 경우, ProductListDto를 반환한다.
- [x] 상품 수정
    - '/admin/product/update/{id}' 경로로 put 요청이 들어올 경우, 변경된 ProductDto를 반환한다.
        - [x] Controller에서는 PathVariable로 id와 RequestBody로 변경될 데이터를 받아 Service에게 전달
        - [x] Service에서는 id로 찾은 Product와 변경된 값으로 새로운 Product 생성후 update
        - [x] DAO는 데이터베이스에 새로운 Product INSERT
        - [x] Controller는 Service에서 받은 새로운 ProductDTO 반환한다.
- [x] 상품 삭제
    - '/admin/product/delete/{id}' 경로로 delete 요청이 들어올 경우, HttpStatus.NO_CONTENT 반환한다.
        - [x] Controller에서는 PathVariable로 id를 서비스에 전달
        - [x] Service에서는 id로 DAO.deleteByID로 DB에서 Product를 삭제한다.

### 관리자 도구 페이지 연동

---

## 1단계 리팩토링 요구사항

- [x] Controller 네이밍 변경
- [x] path에서 http method에 대한 키워드 삭제
- [x] Update의 반환 상태 코드 변경
- [x] @Valid 어노테이션 사용
- [x] Domain 객체에 DTO 객체를 넘겨주지 않도록 변경
- [x] 매직넘버 상수화
- [x] 사용하지 않는 클래스 삭제
- [x] 필요없는 this 제거
- [x] ExceptionHandler 추가
- [x] findById() Optional 처리
- [x] fake 객체의 저장, 조회가 정상적으로 동작하는지 확인하도록 변경
- [x] service 테스트 추가
- [x] 필요없는 출력문 제거
- [x] Integration Test에서 DB에 값이 실제로 저장되는지 확인
- [x] update, delete 테스트 전에 테스트를 위한 데이터를 추가한 후 검증하도록 변경
- [x] Product 테스트 추가
- [x] findById() 테스트 추가
- [ ] 엔드포인트간 DTO 분리?

---

## 2단계 기능 요구사항

### 사용자 기능 구현

- [x] 사용자는 아래와 같은 필드를 가지고 있다.
    ```mermaid
        erDiagram
          MEMBER {
          BIGINT id PK
          VARCHAR email
          VARCHAR password
          }
    ```

### 사용자 설정 페이지 연동

- [x] 컨트롤러는 아래와 같은 요청을 처리한다.
    - [x] '/settings' 경로로 get 요청이 들어올 경우, settings.html 페이지를 반환한다.

- [x] 사용자 서비스는 아래와 같은 기능을 한다.
    - [x] 전체 사용자 목록을 DB에서 가져온다.
    - [x] 전체 사용자 목록을 컨트롤러에 보낸다.

### 장바구니 기능 구현

- [x] 장바구니는 아래와 같은 필드를 가지고 있다.
    ```mermaid
        erDiagram
          CART {
          BIGINT id PK
          BIGINT product_id FK
          BIGINT member_id FK
          }
    ```

- [x] 장바구니 CRUD
    - [x] Basic Auth 방식의 `Authorization` header를 통해 사용자를 인증한다.

    | Method | Path                          |
    |--------|-------------------------------|
    | GET    | /carts                        |
    | POST   | /carts                        |
    | DELETE | /carts/?productId={productId} |


### 장바구니 페이지 연동

---

## 2단계 리팩토링 요구사항

- [x] addViewControllers를 이용한 Configuration 방식에서 실제 Controller의 메서드를 만드는 방식으로 변경
- [x] cart에 있는 상품을 삭제하는 API에서 @PathVariable 대신 @RequestParam을 사용하도록 변경
- [x] admin과 다른 서비스 간 ResponseDto 분리
- [x] 데이터를 찾지 못한 경우, Bad Request 상태 코드를 반환하도록 변경
- [x] 통합 테스트 수정
- [x] 집계 쿼리를 이용하여 상품 수량을 계산하도록 변경
