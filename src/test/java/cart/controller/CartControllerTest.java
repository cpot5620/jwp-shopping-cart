package cart.controller;

import cart.dto.request.ProductRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/resetWebEnvironment.sql")
class CartControllerTest {

    private static final String EXITING_USER_EMAIL = "pooh@naver.com";
    private static final String USER_PASSWORD = "123";
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("로그인하지 않은 유저가 addProduct 요청을 보낸다면 401 status code 를 반환한다")
    @Test
    void addProduct_invalid_notAuthorized() {
        //given,when,then
        RestAssured
                .given().log().all()
                .when().post("/cart/1")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("로그인 요청에 해당하는 유저가 없으면 예외가 발생한다")
    @Test
    void addProduct_invalid_notExistingUser() {
        //given,when,then
        RestAssured
                .given().log().all()
                .auth().preemptive().basic("notExisingUserEmail", "None")
                .when().post("/cart/1")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("추가 요청에서 상품 ID를 숫자로 입력하지 않으면 예외가 발생한다")
    @Test
    void addProduct_invalid_notInteger() {
        //given,when,then
        RestAssured
                .given().log().all()
                .auth().preemptive().basic(EXITING_USER_EMAIL, USER_PASSWORD)
                .contentType(ContentType.JSON)
                .when().post("/cart/hello")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("로그인 정보가 유효하다면 상품 추가 요청을 완료한다")
    @Test
    void addProduct_valid() {
        //given, when,then
        RestAssured
                .given().log().all()
                .auth().preemptive().basic(EXITING_USER_EMAIL, USER_PASSWORD)
                .when().post("/cart/1")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("로그인 한 유저가 해당 아이템을 가지고 있지 않다면 카트 삭제 요청은 실패한다")
    @Test
    void deleteProduct_invalid_nonexistenceProduct() {
        //given, when, then
        RestAssured
                .given()
                .auth().preemptive().basic(EXITING_USER_EMAIL, USER_PASSWORD)
                .contentType(ContentType.JSON)
                .when().delete("/cart/1000")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("로그인 한 유저의 카트 아이템을 삭제한다")
    @Test
    void deleteProduct() {
        //given
        final ProductRequestDto productRequestDto = new ProductRequestDto(1);
        RestAssured
                .given()
                .auth().preemptive().basic(EXITING_USER_EMAIL, USER_PASSWORD)
                .when().post("/cart/1")
                .then()
                .statusCode(HttpStatus.CREATED.value());


        //when, then
        RestAssured
                .given()
                .auth().preemptive().basic(EXITING_USER_EMAIL, USER_PASSWORD)
                .contentType(ContentType.JSON)
                .when().delete("/cart/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
