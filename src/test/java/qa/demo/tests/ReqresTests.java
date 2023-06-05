package qa.demo.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.demo.model.LombokLoginRequestModel;
import qa.demo.model.LombokLoginResponseModel;
import qa.demo.model.PojoLoginRequestModel;
import qa.demo.model.PojoLoginResponseModel;

import java.util.Collections;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static qa.demo.helpers.CustomAllureListener.withCustomTemplates;

@DisplayName("Запросы к reqres API")
public class ReqresTests {

    final String baseUrl = "https://reqres.in/";

    @DisplayName("Авторизация через логин (Pojo)")
    @Test
    void loginWithPojoTest() {
        PojoLoginRequestModel requestModel = new PojoLoginRequestModel();
        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("cityslicka");
        PojoLoginResponseModel responseModel =
                step("Делаем запрос на авторизацию через логин", () ->
                        given()
                                .filter(withCustomTemplates())
                                .log().uri()
                                .log().body()
                                .contentType(JSON)
                                .body(requestModel)
                                .when()
                                .post(baseUrl + "api/login")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(200).extract().as(PojoLoginResponseModel.class));
        step("Проверяем значение полученного токена", () ->
                assertThat(responseModel.getToken()).isEqualTo("QpwL5tke4Pnpja7X4")
        );
    }

    @DisplayName("Авторизация через логин (Lombok)")
    @Test
    void loginWithLombokTest() {
        LombokLoginRequestModel requestModel = new LombokLoginRequestModel();
        requestModel.setEmail("eve.holt@reqres.in");
        requestModel.setPassword("cityslicka");
        LombokLoginResponseModel responseModel =
                step("Делаем запрос на авторизацию через логин", () ->
                        given()
                                .filter(withCustomTemplates())
                                .log().uri()
                                .log().body()
                                .contentType(JSON)
                                .body(requestModel)
                                .when()
                                .post(baseUrl + "api/login")
                                .then()
                                .log().status()
                                .log().body()
                                .statusCode(200).extract().as(LombokLoginResponseModel.class));
        step("Проверяем значение полученного токена", () ->
                assertThat(responseModel.getToken()).isEqualTo("QpwL5tke4Pnpja7X4")
        );
    }

    @DisplayName("Авторизация через логин без передачи \"password\"")
    @Test
    void missingPasswordTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\"}";
        step("Делаем запрос на авторизацию через логин", () -> {
            given()
                    .filter(withCustomTemplates())
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
        });
    }

    @DisplayName("Получение списка пользователей на существующей странице")
    @Test
    void gettingUserListWithNotEmptyData() {
        String pageNumber = "1";
        step("Делаем запрос на получение списка пользователей на указанной странице", () -> {
            given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .when()
                    .get(baseUrl + "api/users?page=" + pageNumber)
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("total", is(12));
        });
    }

    @DisplayName("Получение списка пользователей на несуществующей странице")
    @Test
    void gettingUserListWithEmptyData() {
        String pageNumber = "90";
        step("Делаем запрос на получение списка пользователей на указанной странице", () -> {
            given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .when()
                    .get(baseUrl + "api/users?page=" + pageNumber)
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("data", is(Collections.emptyList()));
        });
    }

    @DisplayName("Получение информации о существующем пользователе")
    @Test
    void gettingExistingUserById() {
        String userId = "2";
        step("Делаем запрос на получение информации о пользователе", () -> {
            given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .when()
                    .get(baseUrl + "api/users/" + userId)
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("data.first_name", is("Janet"))
                    .body("data.last_name", is("Weaver"));
        });
    }

    @DisplayName("Получение информации о несуществующем пользователе")
    @Test
    void gettingNotExistUserById() {
        String userId = "40";
        step("Делаем запрос на получение информации о пользователе", () -> {
            given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .when()
                    .get(baseUrl + "api/users/" + userId)
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(404)
                    .body(is("{}"));
        });
    }

    @DisplayName("Регистрация нового пользователя")
    @Test
    void registrationUserTest() {
        String data = "{\"name\": \"morpheus\",\"job\": \"leader\"}";
        step("Делаем запрос на регистрацию нового пользователе", () -> {
            given()
                    .filter(withCustomTemplates())
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
        });
    }

    @DisplayName("Регистрация нового пользователя без передачи тела запроса")
    @Test
    void registrationUserWithoutBodyTest() {
        step("Делаем запрос на регистрацию нового пользователе", () -> {
            given()
                    .filter(withCustomTemplates())
                    .log().uri()
                    .contentType(JSON)
                    .when()
                    .post(baseUrl + "api/users")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(201)
                    .body("id", not(empty()));
        });
    }
}
