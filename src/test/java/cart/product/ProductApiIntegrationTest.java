package cart.product;

import cart.product.dao.ProductDao;
import cart.product.domain.Money;
import cart.product.domain.Product;
import cart.product.domain.ProductImageUrl;
import cart.product.domain.ProductName;
import cart.product.dto.request.ProductAddRequest;
import cart.product.dto.request.ProductUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static cart.TestUtils.toJson;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProductApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("상품이 정상적으로 추가되었을 때 CREATED 응답 코드를 반환한다")
    void add() throws JsonProcessingException {
        final ProductAddRequest productAddRequest = new ProductAddRequest("a".repeat(ProductName.MAX_LENGTH), "a".repeat(ProductImageUrl.MAX_LENGTH), Money.MAX_AMOUNT);
        final int productId = 1;

        given()
                .contentType(ContentType.JSON)
                .body(toJson(productAddRequest))
        .when()
                .post("/products")
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/products/" + productId);

        final Product actual = productDao.findById(1L).get();

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(productAddRequest.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(productAddRequest.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(productAddRequest.getImageUrl())
        );
    }

    @ParameterizedTest(name = "상품 이름이 공백만으로 구성되면 BAD REQUEST 응답 코드를 반환한다")
    @ValueSource(strings = {" ", ""})
    void addFailShortName(String name) throws JsonProcessingException {
        final ProductAddRequest productAddRequest = new ProductAddRequest(name, "a".repeat(ProductImageUrl.MAX_LENGTH), Money.MAX_AMOUNT);

        given()
                .contentType(ContentType.JSON)
                .body(toJson(productAddRequest))
        .when()
                .post("/products")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.containsString("이름"));
    }

    @Test
    @DisplayName("상품 이름 길이가 기준을 넘으면 BAD REQUEST 응답 코드를 반환한다")
    void addFailLongName() throws JsonProcessingException {
        final ProductAddRequest productAddRequest = new ProductAddRequest("a".repeat(ProductName.MAX_LENGTH + 1), "a".repeat(ProductImageUrl.MAX_LENGTH), Money.MAX_AMOUNT);

        given()
                .contentType(ContentType.JSON)
                .body(toJson(productAddRequest))
        .when()
                .post("/products")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.containsString("이름"));
    }

    @ParameterizedTest(name = "상품 URL이 공백만으로 구성되면 BAD REQUEST 응답 코드를 반환한다")
    @ValueSource(strings = {" ", ""})
    void addFailShortUrl(String url) throws JsonProcessingException {
        final ProductAddRequest productAddRequest = new ProductAddRequest("a".repeat(ProductName.MAX_LENGTH), url, Money.MAX_AMOUNT);

        given()
                .contentType(ContentType.JSON)
                .body(toJson(productAddRequest))
        .when()
                .post("/products")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.containsStringIgnoringCase("url"));
    }

    @Test
    @DisplayName("상품 사진 url 길이가 기준을 넘으면 BAD REQUEST 응답 코드를 반환한다")
    void addFailLongUrl() throws JsonProcessingException {
        final ProductAddRequest productAddRequest = new ProductAddRequest("a".repeat(ProductName.MAX_LENGTH), "a".repeat(ProductImageUrl.MAX_LENGTH + 1), Money.MAX_AMOUNT);

        given()
                .contentType(ContentType.JSON)
                .body(toJson(productAddRequest))
        .when()
                .post("/products")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", Matchers.containsStringIgnoringCase("url"));
    }

    @ParameterizedTest(name = "상품이 가격이 허용 범위 밖이면 BAD REQUEST 응답 코드를 반환한다")
    @ValueSource(ints = {-1, 1_000_000_001})
    void addFailPrice(int price) throws JsonProcessingException {
        final ProductAddRequest productAddRequest = new ProductAddRequest("a".repeat(ProductName.MAX_LENGTH), "a".repeat(ProductImageUrl.MAX_LENGTH), price);

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(toJson(productAddRequest))
        .when()
               .post("/products")
        .then()
               .statusCode(HttpStatus.BAD_REQUEST.value())
               .body("message", containsString("금액"));
    }

    @Test
    @DisplayName("상품 수정이 성공하면 OK 상태 코드를 반환한다.")
    void update() throws JsonProcessingException {
        productDao.save(new Product("에밀", "emil.com", 1000));

        final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "a".repeat(ProductName.MAX_LENGTH), "a".repeat(ProductImageUrl.MAX_LENGTH), Money.MAX_AMOUNT);

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(toJson(productUpdateRequest))
        .when()
               .put("/products")
        .then()
               .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("존재하지 않는 상품을 요청하면 NOT_FOUND 상태 코드를 반환한다.")
    void updateFailNoId() throws JsonProcessingException {
        productDao.save(new Product("에밀", "emil.com", 1000));

        final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(2L, "a".repeat(ProductName.MAX_LENGTH), "a".repeat(ProductImageUrl.MAX_LENGTH), Money.MAX_AMOUNT);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(toJson(productUpdateRequest))
        .when()
                .put("/products")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", Matchers.containsStringIgnoringCase("상품"));
    }

    @Test
    @DisplayName("상품 삭제가 성공하면 NO CONTENT 상태 코드를 반환한다.")
    void delete() {
        productDao.save(new Product("에밀", "emil.com", 1000));

        when()
            .delete("/products/1")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("존재하지 않는 상품을 삭제하면 NOT_FOUND 상태 코드를 반환한다.")
    void deleteFailNoId() {
        productDao.save(new Product("에밀", "emil.com", 1000));

        when()
                .delete("/products/" + 2)
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", Matchers.containsStringIgnoringCase("상품"));
    }
}
