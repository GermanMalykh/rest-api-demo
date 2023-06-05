package qa.demo;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresTests {

    final String baseUrl = "https://reqres.in/";

    @Test
    void loginTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(baseUrl + "api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void missingEmailOrUsernameTest() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .post(baseUrl + "api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void missingPasswordTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(baseUrl + "api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void gettingUserListWithNotEmptyData() {
        String pageNumber = "1";
        given()
                .log().uri()
                .when()
                .get(baseUrl + "api/users?page=" + pageNumber)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12));
    }

    @Test
    void gettingUserListWithEmptyData() {
        String pageNumber = "90";
        given()
                .log().uri()
                .when()
                .get(baseUrl + "api/users?page=" + pageNumber)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data", is(Collections.emptyList()));
    }

    @Test
    void gettingExistingUserById() {
        String userId = "2";
        given()
                .log().uri()
                .when()
                .get(baseUrl + "api/users/" + userId)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"));
    }

    @Test
    void gettingNotExistUserById() {
        String userId = "40";
        given()
                .log().uri()
                .when()
                .get(baseUrl + "api/users/" + userId)
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    void registrationUserTest() {
        String data = "{\"name\": \"morpheus\",\"job\": \"leader\"}";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post(baseUrl + "api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"));
    }

    @Test
    void registrationUserWithoutBodyTest() {
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .post(baseUrl + "api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("id", not(empty()));
    }
}
