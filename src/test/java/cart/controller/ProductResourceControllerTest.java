package cart.controller;

import cart.dto.request.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql("classpath:test.sql")
class ProductResourceControllerTest {

    private final String validName = "족발";
    private final int validPrice = 10_000;
    private final String validImageUrl = "https://naver.com";
    private final ProductRequest validProduct = new ProductRequest(validName, validPrice, validImageUrl);
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 상품_추가() {
        RestAssured.given()
                .body(validProduct)
                .contentType(ContentType.JSON)
                .when()
                .post("/admin/products")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 상품_수정() {
        final long id = 1L;

        RestAssured.given()
                .body(validProduct)
                .contentType(ContentType.JSON)
                .when()
                .put("/admin/products/" + id)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 상품_삭제() {
        final long id = 2L;

        RestAssured.given()
                .when()
                .delete("/admin/products/" + id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void 상품의_이름이_빈값_널_공백이면_Bad_Request_발생(final String invalidName) {
        final ProductRequest invalidProduct = new ProductRequest(
                invalidName,
                validPrice,
                validImageUrl
        );

        RestAssured.given()
                .body(invalidProduct)
                .contentType(ContentType.JSON)
                .when()
                .post("/admin/products")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @ValueSource(ints = {21, 22})
    void 상품의_이름이_20자를_초과하는_경우_Bad_Request_발생(final int length) {
        final String invalidName = "a".repeat(length);
        final ProductRequest invalidProduct = new ProductRequest(
                invalidName,
                validPrice,
                validImageUrl
        );

        RestAssured.given()
                .body(invalidProduct)
                .contentType(ContentType.JSON)
                .when()
                .post("/admin/products")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 100_000_001})
    void 범위가_벗어난_가격을_입력시_Bad_Request_발생(final int invalidPrice) {
        final ProductRequest invalidProduct = new ProductRequest(
                validName,
                invalidPrice,
                validImageUrl
        );

        RestAssured.given()
                .body(invalidProduct)
                .contentType(ContentType.JSON)
                .when()
                .post("/admin/products")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "github.com", "ssh://git@github.com"})
    @NullSource
    void 유효하지_않은_URL_입력시_Bad_Request_발생(final String invalidUrl) {
        final ProductRequest invalidProduct = new ProductRequest(
                validName,
                validPrice,
                invalidUrl
        );

        RestAssured.given()
                .body(invalidProduct)
                .contentType(ContentType.JSON)
                .when()
                .post("/admin/products")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @ValueSource(ints = {2084, 2085})
    void 길이_제한을_벗어난_URL을_입력하면_Bad_Request_발생(final int length) {
        final int repeatCount = length - 12;
        final String invalidUrl = "https://" + "a".repeat(repeatCount) + ".com";
        final ProductRequest invalidProduct = new ProductRequest(
                validName,
                validPrice,
                invalidUrl
        );

        RestAssured.given()
                .body(invalidProduct)
                .contentType(ContentType.JSON)
                .when()
                .post("/admin/products")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
