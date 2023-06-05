package qa.demo.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.Test;
import qa.demo.model.LombokLoginRequestModel;
import qa.demo.model.LombokLoginResponseModel;
import qa.demo.model.PojoLoginRequestModel;
import qa.demo.model.PojoLoginResponseModel;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class ReqresTests {

    final String baseUrl = "https://reqres.in/";

    @Test
    void loginWithPojoTest() {
        PojoLoginRequestModel requestModel = new PojoLoginRequestModel();
        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("cityslicka");

        PojoLoginResponseModel responseModel =
                given()
                        .filter(new AllureRestAssured())
                        .log().uri()
                        .log().body()
                        .contentType(JSON)
                        .body(requestModel)
                        .when()
                        .post(baseUrl + "api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200).extract().as(PojoLoginResponseModel.class);
        assertThat(responseModel.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void loginWithLombokTest() {
        LombokLoginRequestModel requestModel = new LombokLoginRequestModel();
        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("cityslicka");

        LombokLoginResponseModel responseModel =
                given()
                        .filter(new AllureRestAssured())
                        .log().uri()
                        .log().body()
                        .contentType(JSON)
                        .body(requestModel)
                        .when()
                        .post(baseUrl + "api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200).extract().as(LombokLoginResponseModel.class);
        assertThat(responseModel.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void missingEmailOrUsernameTest() {
        given()
                .filter(new AllureRestAssured())
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
                .filter(new AllureRestAssured())
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
                .filter(new AllureRestAssured())
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
                .filter(new AllureRestAssured())
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
                .filter(new AllureRestAssured())
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
                .filter(new AllureRestAssured())
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
                .filter(new AllureRestAssured())
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
                .filter(new AllureRestAssured())
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
