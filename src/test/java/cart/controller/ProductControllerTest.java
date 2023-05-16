package cart.controller;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dto.ProductRequest;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    int port;

    static Stream<Arguments> makeDto() {
        return Stream.of(Arguments.arguments(new ProductRequest("a".repeat(256), "https://naver.com", 1000)),
            Arguments.arguments(new ProductRequest("aaa", "https://naver" + "a".repeat(8001) + ".com", 1000)),
            Arguments.arguments(new ProductRequest("aaa", "https://naver.com", -1000)));
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("상품을 추가한다.")
    void addProduct() {
        ProductRequest productRequest = new ProductRequest("리오", "http://asdf.asdf", 3000);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when().post("/products")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest
    @MethodSource("makeDto")
    @DisplayName("상품을 추가한다. - 잘못된 입력을 검증한다.")
    void addProductInvalidInput(ProductRequest productRequest) {

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when().post("/products")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("상품 정보를 수정한다.")
    void updateProduct() {
        ProductRequest productRequest = new ProductRequest("리오", "http://asdf.asdf", 3000);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when().put("/products/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @MethodSource("makeDto")
    @DisplayName("상품 정보를 수정한다. - 잘못된 입력을 검증한다.")
    void updateProductInvalidInput(ProductRequest productRequest) {

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(productRequest)
            .when().put("/products/1")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void deleteProduct() {
        RestAssured.given().log().all()
            .when().delete("/products/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }
}
