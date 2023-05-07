# jwp-shopping-cart

# 장바구니 미션 

## 💡프로젝트 개요
- 상품 목록을 보고 장바구니에 담을 수 있는 웹 애플리케이션 프로젝트입니다.
---

## API 명세
### GET
- View 요청
  - GET /index : index 페이지를 요청합니다.
  - GET /admin : admin 페이지를 요청합니다.
  - GET /cart : cart 페이지를 요청합니다.
  - GET /settings : settings 페이지를 요청합니다.

- Cart
  - GET /api/cart/items : 사용자의 인증 정보를 바탕으로 장바구니 품목들을 요청합니다.

### POST
- Product
  - POST /api/products : body에 담긴 정보를 바탕으로 새로운 상품을 생성합니다.

- Cart
  - POST /api/cart/items : Query Parameter로 넘어온 상품 Id 값을 바탕으로 장바구니에 품목을 생성합니다.

### PUT
- Product
  - PUT /api/products/{id} : body에 담긴 정보를 바탕으로 기존 상품 정보를 수정합니다.

### DELETE
- Product
  - DELETE /api/products/{id} : Path Variable에 담긴 상품 Id정보를 바탕으로 기존 상품 정보를 삭제합니다.

- Cart
  - DELETE /api/cart/items/{id} : Path Variable에 담긴 품목 Id정보를 바탕으로 장바구니 품목을 삭제합니다.


## 📋 구현 기능 목록

## Task 정리
- [X] 상품 목록 페이지 연동
  - [X] '/products' url로 이동 시 product 페이지 볼 수 있도록 연동하기
  - [X] 사용자가 원하는 상품을 장바구니에 담을 수 있다.
- [X] 관리자 도구 페이지 연동
  - [X] '/admin' url로 이동 시 product 페이지 볼 수 있도록 연동하기
- [X] 상품관리 CRUD API 작성
  - [X] 상품 목록 페이지는 등록된 모든 상품을 Read 한다.
- [X] 관리자 도구 페이지 CRUD API 작성
  - [X] 관리자 도구 페이지는 등록되어 있는 단일 상품의 정보를 Create 한다.
  - [X] 관리자 도구 페이지는 등록된 모든 상품을 Read 한다.
  - [X] 관리자 도구 페이지는 등록되어 있는 단일 상품의 정보를 Update 한다.
  - [X] 관리자 도구 페이지는 등록되어 있는 단일 상품의 정보를 Delete 한다.
- 사용자 설정 페이지 연동
  - [x] '/settings' url로 이동 시 모든 사용자 정보를 확인할 수 있다.
  - [x] 사용자 설정 페이지에서 사용자를 선택할 수 있다.
  - [x] 사용자를 선택하면, 이후 요청 메세지에는 선택한 사용자의 인증 정보가 포함되어야 한다.
- 장바구니 페이지 연동
  - [x] '/cart' url로 이동 시 사용자의 장바구니에 담긴 상품들을 조회할 수 있다.
  - [x] 사용자가 원하는 상품을 담을 수 있다.
  - [x] 사용자가 원하는 상품을 삭제할 수 있다.

### Business Layer
#### 사용자 인증
- [x] 요청한 사용자의 정보가 등록되어 있는 정보인지 인증한다.

#### domain(model)
- [X] 상품 기본 정보는 상품 ID, 상품 이름, 상품 이미지, 상품 가격들을 포함한다.
- [X] 사용자 정보는 사용자 이메일, 패스워드를 포함한다.
- [x] 장바구니 정보는 사용자 ID, 상품 ID 리스트를 포함한다.
  - [x] 사용자가 추가하기를 요청한 상품 ID가 이미 담은 상품인지 확인할 수 있다.
- [X] 도메인에 종속된 검증 기능
  - [X] 상품 이름 길이는 1 ~ 10자로 제한한다.
  - [X] 가격 산정 범위는 1000원 ~ 100만원 사이다.
  - [x] 이미 장바구니에 담겨있는 상품ID를 추가할 수 없다.

## 예외 처리
- [X] 요청 메세지 값의 null, 공백 검증


## Todo
