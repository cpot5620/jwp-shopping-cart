## 기능 목록 명세

### 도메인

- Product
    - [x] 상품 이름 : 50자 이상인 경우 예외를 던진다.
    - [x] 가격 : 0 미만 10억 초과인 경우 예외를 던진다.
    - [x] 이미지 : 2000자 초과일 경우 예외를 던진다.

- Member
    - [x] email
        - 이메일 형식
            - [x] 이메일은 계정@도메인 형식을 갖는다.
            - [x] 이메일 형식에 어긋나면 예외가 발생한다.
    - [x] password
        - 비밀번호 형식
            - [x] 비밀번호는 최소 8자 이상, 최대 20자 이하이다.
            - [x] 다음으로 구성된다.
                - 최소 하나의 숫자
                - 최소 하나의 문자 : 단, 대소문자는 구별한다.
                - 최소 하나의 특수문자 : ~`!@#$%^&*()-+=
            - [x] 비밀번호 형가에 어긋나면 예외가 발생한다.

- ProductEntity
    - 상품
        - id
        - 이름
        - 이미지
        - 가격

- MemberEntity
    - 사용자
        - id
        - 이메일
        - 비밀번호

- CartEntity
    - 장바구니
        - id
        - 사용자 id
        - 상품 id

### API

- [x] 상품 목록 페이지 연동
    - [x] 요청 : Get /
    - [x] 응답 : index.html
        - [x] 모델 추가 : List\<ResponseProductDto>

- [x] 관리자 도구 페이지 연동
    - [x] 요청 : Get /admin
    - [x] 응답 : admin.html
        - [x] 모델 추가 : List\<ResponseProductDto>

- [x] 사용자 설정 페이지 연동
    - [x] 요청 : Get /settings
    - [x] 응답 : settings.html

- [x] 상품 관리 API 작성
  /product
    - [x] Create
        - [x] 요청 : Post
            - [x] 상품 이름, 가격, 이미지
        - [x] 응답 : 201
        - [x] 예외 :
            - [x] 입력값이 비어있는 경우
            - [x] 입력값이 도메인의 조건을 만족하지 않은 경우
    - [x] Update
        - [x] 요청 : put
            - id 전송
            -  [x] 상품 이름, 가격, 이미지
        - [x] 응답 : 200 /admin
        - [x] 예외 :
            - [x] 입력값이 비어있는 경우
            - [x] 입력값이 도메인의 조건을 만족하지 않은 경우
            - [x] DB 관련 예외가 생긴 경우
    - [x] Delete
        - [x] 요청 : delete
        - id 전송
        - [x] 응답 : 200 /admin
        - [x] 예외 :
            - [x] DB 관련 예외가 생긴 경우

- [x] 장바구니 기능 API 작성
    - /carts
        - [x] Create : 장바구니에 상품 추가
            - [x] 요청 : Post
                - [x] 상품 id
            - [x] 응답 : 200

        - [x] Read : 장바구니 품목 읽기
            - [x] 요청 : Get
            - [x] 응답 : 200

        - [x] Delete : 장바구니에 담긴 상품 제거
            - [x] 요청 : Delete
                - [x] 상품 id
            - [x] 응답 : 200

- [x] 사용자 선택
    - [x] 사용자 인증 정보
        - [x] Header : Authorization
        - [x] type : Basic
        - [x] authInfo : email:password (encoded with base64)

## DB 테이블

```sql
CREATE TABLE product
(
    id    INT           NOT NULL AUTO_INCREMENT,
    name  VARCHAR(50)   NOT NULL,
    price INT           NOT NULL,
    image VARCHAR(2000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE member
(
    id       INT          NOT NULL AUTO_INCREMENT,
    email    VARCHAR(320) NOT NULL,
    password VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id)
)

CREATE TABLE cart
(
    id         INT NOT NULL AUTO_INCREMENT,
    member_id  INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
)
```

## 테스트

### Dao

- [x] @JdbcTest
    - [x] Transaction 관리 가능

### Service

- [x] MockitoExtension
    - [x] Mock 을 활용한 단위 테스트
    - [x] 실제 DB와 연결되지 않는 테스트

### Controller

- [x] @SpringBootTest
    - [x] 통합 테스트

- [x] @WebMvcTest
    - [x] Mock 을 활용한 단위 테스트
    - [x] 실제 Service 와 연결되지 않는 테스트

### 인수 테스트

- [x] @SpringBootTest
    - [x] 시나리오 기반 테스트

## 기록

- email이 중복되는 것은 어디에서 봐줘야 하는가?
