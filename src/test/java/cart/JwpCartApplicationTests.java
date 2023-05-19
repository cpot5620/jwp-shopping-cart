package cart;

import cart.controller.dto.request.ItemRequest;
import cart.controller.dto.request.UserRequest;
import cart.controller.dto.response.ItemResponse;
import cart.controller.dto.response.UserResponse;
import cart.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql("classpath:initializeTestDb.sql")
class JwpCartApplicationTests {

    public static final List<ItemResponse> EXPECTED_PRODUCTS = List.of(
            ItemResponse.from(new Item.Builder().id(1L)
                                                .name(new Name("위키드"))
                                                .imageUrl(new ImageUrl("https://image.yes24.com/themusical/upFiles/Themusical/Play/post_2013wicked.jpg"))
                                                .price(new Price(150000))
                                                .build()),
            ItemResponse.from(new Item.Builder().id(2L)
                                                .name(new Name("마틸다"))
                                                .imageUrl(new ImageUrl("https://ticketimage.interpark.com/Play/image/large/22/22009226_p.gif"))
                                                .price(new Price(100000))
                                                .build()),
            ItemResponse.from(new Item.Builder().id(3L)
                                                .name(new Name("빌리 엘리어트"))
                                                .imageUrl(new ImageUrl("https://t1.daumcdn.net/cfile/226F4D4C544F42CF34"))
                                                .price(new Price(200000))
                                                .build())
    );
    public static final List<UserResponse> EXPECTED_MEMBERS = List.of(
            UserResponse.from(new User.Builder().id(1L)
                                                .email(new Email("test1@gmail.com"))
                                                .password(new Password("test1pw1234"))
                                                .build()
            ),
            UserResponse.from(new User.Builder().id(2L)
                                                .email(new Email("test2@naver.com"))
                                                .password(new Password("test2pw5678"))
                                                .build()
            ),
            UserResponse.from(new User.Builder().id(3L)
                                                .email(new Email("test3@gmail.com"))
                                                .password(new Password("test3pw9090"))
                                                .build()
            )
    );

    @LocalServerPort
    private int port;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("GET / 요청 정상 응답")
    @Test
    void contextLoadsView() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(model().attribute("products", EXPECTED_PRODUCTS))
               .andExpect(view().name("index"))
               .andExpect(status().isOk());
    }

    @DisplayName("GET /admin 요청 정상 응답")
    @Test
    void getRequestAdminView() throws Exception {
        mockMvc.perform(get("/admin"))
               .andExpect(model().attribute("products", EXPECTED_PRODUCTS))
               .andExpect(view().name("admin"))
               .andExpect(status().isOk());
    }

    @DisplayName("GET /settings 요청 정상 응답")
    @Test
    void getRequestSettingView() throws Exception {
        mockMvc.perform(get("/settings"))
               .andExpect(model().attribute("members", EXPECTED_MEMBERS))
               .andExpect(view().name("settings"))
               .andExpect(status().isOk());
    }

    @DisplayName("GET /cart 요청 정상 응답")
    @Test
    void getRequestCartView() throws Exception {
        mockMvc.perform(get("/cart"))
               .andExpect(view().name("cart"))
               .andExpect(status().isOk());
    }

    @DisplayName("POST /items 요청 정상 응답")
    @Test
    void postRequestItem() throws Exception {
        String content = objectMapper.writeValueAsString(new ItemRequest("레드북", 150000, "url"));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .post("/items")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.CREATED.value())
                   .header("Location", "/");
    }

    @DisplayName("GET /items 요청 정상 응답")
    @Test
    void getRequestItem() {
        RestAssured.given()
                   .log()
                   .all()
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .get("/items")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.OK.value())
                   .body("size()", is(3));
    }

    @DisplayName("PUT /items/{id} 요청 정상 응답")
    @Test
    void putRequestItem() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(new ItemRequest("레드북", 150000, "url"));
        RestAssured.given()
                   .log()
                   .all()
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .body(content)
                   .when()
                   .log()
                   .all()
                   .put("/items/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.CREATED.value())
                   .header("Location", "/");
    }

    @DisplayName("DELETE /items/{id} 요청 정상 응답")
    @Test
    void deleteRequestItem() {
        RestAssured.given()
                   .log()
                   .all()
                   .when()
                   .log()
                   .all()
                   .delete("/items/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.OK.value())
                   .header("Location", "/");
    }

    @DisplayName("POST /items 요청 예외 응답")
    @Test
    void postRequestItemException() throws Exception {
        String content = objectMapper.writeValueAsString(new ItemRequest("", 150000, "url"));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .post("/items")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("이름을 입력해주세요."));
    }

    @DisplayName("PUT /items/{id} 요청 예외 응답")
    @Test
    void putRequestItemException() throws Exception {
        String content = objectMapper.writeValueAsString(new ItemRequest("", 150000, "url"));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .put("/items/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("이름을 입력해주세요."));
    }

    @DisplayName("PUT /items/{id} 요청 예외 응답")
    @Test
    void putRequestItemExceptionWithNotExist() throws Exception {
        String content = objectMapper.writeValueAsString(new ItemRequest("레드북", 150000, "url"));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .put("/items/100")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("존재하지 않는 아이템 입니다."));
    }

    @DisplayName("DELETE /items/{id} 요청 예외 응답")
    @Test
    void deleteRequestItemException() {
        RestAssured.when()
                   .delete("/items/100")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("존재하지 않는 아이템 입니다."));
    }

    @DisplayName("POST /users 요청 정상 응답")
    @Test
    void postRequestUser() throws Exception {
        String content = objectMapper.writeValueAsString(new UserRequest("test@email.com", "testPW"));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .post("/users")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.CREATED.value())
                   .header("Location", "/settings");
    }

    @DisplayName("POST /users 요청 예외 응답")
    @Test
    void postRequestUserException() throws Exception {
        String content = objectMapper.writeValueAsString(new UserRequest("", "testPW"));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .post("/users")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("이메일은 빈 값일 수 없습니다."));
    }

    @DisplayName("GET /users 요청 정상 응답")
    @Test
    void getRequestAllUser() {
        RestAssured.given()
                   .log()
                   .all()
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .get("/users")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.OK.value())
                   .body("size()", is(3));
    }

    @DisplayName("GET /users/id 요청 정상 응답")
    @Test
    void getRequestUser() {
        RestAssured.given()
                   .log()
                   .all()
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .get("/users/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("PUT /users/{id} 요청 정상 응답")
    @Test
    void putRequestUser() throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(new UserRequest("edit@email.com", "editPW"));
        RestAssured.given()
                   .log()
                   .all()
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .body(content)
                   .when()
                   .log()
                   .all()
                   .put("/users/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.CREATED.value())
                   .header("Location", "/settings");
    }


    @DisplayName("PUT /users/{id} 요청 예외 응답")
    @Test
    void putRequestUserException() throws Exception {
        String content = objectMapper.writeValueAsString(new UserRequest("test@email.com", ""));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .put("/users/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("비밀번호는 빈 값일 수 없습니다."));
    }

    @DisplayName("PUT /users/{id} 요청 예외 응답")
    @Test
    void putRequestUserExceptionWithNotExist() throws Exception {
        String content = objectMapper.writeValueAsString(new UserRequest("test@email.com", "testPW"));
        RestAssured.given()
                   .log()
                   .all()
                   .body(content)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .log()
                   .all()
                   .put("/users/100")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("존재하지 않는 사용자입니다."));
    }

    @DisplayName("DELETE /users/{id} 요청 정상 응답")
    @Test
    void deleteRequestUser() {
        RestAssured.given()
                   .log()
                   .all()
                   .when()
                   .log()
                   .all()
                   .delete("/users/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.OK.value())
                   .header("Location", "/settings");
    }


    @DisplayName("DELETE /users/{id} 요청 예외 응답")
    @Test
    void deleteRequestUserException() {
        RestAssured.when()
                   .delete("/users/100")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("존재하지 않는 사용자입니다."));
    }

    @DisplayName("POST /carts/itemId 요청 정상 응답")
    @Test
    void postRequestCart() {
        String email = "test1@gmail.com";
        String password = "test1pw1234";
        RestAssured.given()
                   .log()
                   .all()
                   .auth()
                   .preemptive()
                   .basic(email, password)
                   .when()
                   .log()
                   .all()
                   .post("/carts/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.CREATED.value())
                   .header("Location", "/");
    }

    @DisplayName("POST /carts/itemId 요청 예외 응답")
    @Test
    void postRequestCartException() {
        String email = "test1@gmail.com";
        String password = "test1pw1234";
        RestAssured.given()
                   .log()
                   .all()
                   .auth()
                   .preemptive()
                   .basic(email, password)
                   .when()
                   .log()
                   .all()
                   .post("/carts/100")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("존재하지 않는 아이템 입니다."));
    }

    @DisplayName("GET /carts 요청 정상 응답")
    @Test
    void getRequestAllCart() {
        String email = "test1@gmail.com";
        String password = "test1pw1234";
        RestAssured.given()
                   .log()
                   .all()
                   .auth()
                   .preemptive()
                   .basic(email, password)
                   .when()
                   .log()
                   .all()
                   .get("/carts")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.OK.value())
                   .body("size()", is(2));
    }

    @DisplayName("DELETE /carts/{cartId} 요청 정상 응답")
    @Test
    void deleteRequestCart() {
        String email = "test1@gmail.com";
        String password = "test1pw1234";
        RestAssured.given()
                   .log()
                   .all()
                   .auth()
                   .preemptive()
                   .basic(email, password)
                   .when()
                   .log()
                   .all()
                   .delete("/carts/1")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.OK.value())
                   .header("Location", "/");
    }

    @DisplayName("DELETE /carts/{cartId} 요청 예외 응답")
    @Test
    void deleteRequestCartException() {
        String email = "test1@gmail.com";
        String password = "test1pw1234";
        RestAssured.given()
                   .log()
                   .all()
                   .auth()
                   .preemptive()
                   .basic(email, password)
                   .when()
                   .log()
                   .all()
                   .delete("/carts/100")
                   .then()
                   .log()
                   .all()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body(containsString("존재하지 않는 장바구니 아이템 입니다."));
    }

    @DisplayName("POST /carts/itemId 요청 시 잘못된 인증 정보를 넘기는 경우의 응답")
    @Test
    void postRequestCartExceptionAuth() {
        String authorization = "Basic dGVzdDFAZ21haWwuY29tOnRlc3QxcHcxMjM0 more credential";
        final ExtractableResponse<Response> response = RestAssured.given()
                                                                  .log()
                                                                  .all()
                                                                  .header("Authorization", authorization)
                                                                  .when()
                                                                  .log()
                                                                  .all()
                                                                  .post("/carts/1")
                                                                  .then()
                                                                  .log()
                                                                  .all()
                                                                  .statusCode(HttpStatus.BAD_REQUEST.value())
                                                                  .extract();

        JsonPath result = response.jsonPath();
        assertAll(
                () -> assertThat(result.getString("code")).isEqualTo("400"),
                () -> assertThat(result.getString("status")).isEqualTo("Bad Request"),
                () -> assertThat(result.getString("message")).isEqualTo("잘못된 인증 정보입니다.")
        );
    }

    @DisplayName("POST /carts/itemId 요청 시 없는 계정에 대한 정보를 Auth로 넘기는 경우의 응답")
    @Test
    void postRequestCartNotExistAuth() {
        String email = "notExist@gmail.com";
        String password = "notExistPassword";
        final ExtractableResponse<Response> response = RestAssured.given()
                                                                  .log()
                                                                  .all()
                                                                  .auth()
                                                                  .preemptive()
                                                                  .basic(email, password)
                                                                  .when()
                                                                  .log()
                                                                  .all()
                                                                  .post("/carts/1")
                                                                  .then()
                                                                  .log()
                                                                  .all()
                                                                  .statusCode(HttpStatus.BAD_REQUEST.value())
                                                                  .extract();

        JsonPath result = response.jsonPath();
        assertAll(
                () -> assertThat(result.getString("code")).isEqualTo("400"),
                () -> assertThat(result.getString("status")).isEqualTo("Bad Request"),
                () -> assertThat(result.getString("message")).isEqualTo("존재하지 않는 사용자입니다.")
        );
    }
}
