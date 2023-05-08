# jwp-shopping-cart

## 기능 요구 사항

- [x] 상품 기본 정보는 상품 ID, 상품 이름, 상품 이미지 url, 상품 가격이다.
  - 상품 이름은 1자 이상 20자 이하이다.
  - 상품 가격은 10원 단위의 양수여야 한다.
  - 상품 이미지 url은 512자 이하의 문자열이여야 한다.
- - - -

- [x] 상품 목록 페이지
  - [x] 전체 상품 조회(/)
  

- [x] 장바구니 페이지
  - [x] 조회 (/cart-products GET)
    - [x] authentication 정보가 존재하지 않을 때: `UNAUTHORIZED`
    - [x] 헤더의 authentication 정보가 유효하지 않을 때: `UNAUTHORIZED`
  - [x] 상품 추가 (/cart-products POST)
    - [x] authentication 정보가 존재하지 않을 때: `UNAUTHORIZED`
    - [x] 바디에 양의 정수가 아닌 productId가 포함되었을 때: `BAD_REQUEST`
    - [x] 바디에 존재하지 않는 productId가 포함되었을 때: `BAD_REQUEST`
  - [x] 상품 삭제 (/cart-products/{cartId})
    - [x] authentication 정보가 존재하지 않을 때: `UNAUTHORIZED` 
    - [x] 헤더의 authentication 정보가 유효하지 않을 때: `UNAUTHORIZED`
    - [x] URI에 잘못된 cartId가 포함되었을 때: `BAD_REQUEST`
      
(Basic 인증된 email - password 쌍 외에는 `UNAUTHORIZED` 반환)

- [x] 사용자 설정 페이지
  - [x] 전체 사용자 이메일, 비밀번호 조회(/settings GET)


- [x] 관리자 도구 페이지 상품 관리 CRUD API
  - [x] 생성 (/admin/products POST)
    - 상품의 형식이 올바르지 않을 때: `BAD_REQUEST`
  - [x] 전체 상품 조회 (/admin)
  - [x] 수정 (/admin/products/{id} PUT)
    - 상품의 형식이 올바르지 않을 때: `BAD_REQUEST`
    - 요청된 id에 대한 데이터가 없을 시: `BAD_REQUEST`
  - [x] 삭제 (/admin/products/{id} DELETE)
    - 요청된 id에 대한 데이터가 없을 시: `BAD_REQUEST`

  - 이외의 예외가 발생했을 때: `INTERNAL_SERVER_ERROR`

