package cart.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/resetWebEnvironment.sql")
class ViewControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("root directory 로 요청을 보내면 홈 html 화면을 보내준다")
    void rootPage() {
        //expect
        RestAssured.given().log().headers()
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    @DisplayName(" /admin driectory 로 요청을 보내면 admin html 화면을 보내준다")
    void admin() {
        //expect
        RestAssured.given().log().headers()
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    @DisplayName(" /settings 로 요청을 보내면 settings html 화면을 보내준다")
    void settings() {
        //expect
        RestAssured.given().log().headers()
                .when().get("/settings")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

}
