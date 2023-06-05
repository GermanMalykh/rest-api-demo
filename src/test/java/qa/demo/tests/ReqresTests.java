package qa.demo.tests;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.demo.client.ReqresClient;
import qa.demo.config.TestBase;
import qa.demo.model.LoginResponseModel;
import qa.demo.model.UserRegistrationRequestModel;
import qa.demo.model.LoginRequestModel;
import qa.demo.model.UserRegistrationResponseModel;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Запросы к reqres API")
public class ReqresTests extends TestBase {
    LoginRequestModel loginRequest = new LoginRequestModel();
    UserRegistrationRequestModel userRegistration = new UserRegistrationRequestModel();
    ReqresClient reqresClient = new ReqresClient();
    protected ValidatableResponse response;

    @DisplayName("Успешная регистрация пользователя")
    @Test
    void successfulUserRegistration() {
        step("Указываем \"name\" и \"job\" перед выполнением запроса", () -> {
            userRegistration.setName("ivan");
            userRegistration.setJob("manager");
        });
        step("Делаем запрос на регистрацию пользователя", () -> {
            response = reqresClient.userRegistration(userRegistration);
        });
        step("Статус код в ответе равен 201", () -> {
            response.statusCode(201);
        });
        step("Имя переданное в запросе равно имени в ответе", () -> {
            UserRegistrationResponseModel userRegistrationResponseModel = response.extract().as(UserRegistrationResponseModel.class);
            assertTrue(userRegistrationResponseModel.getName().contains("ivan"));
        });
    }

    @DisplayName("Успешная авторизация пользователя")
    @Test
    void successfulUserAuthorization() {
        step("Указываем \"email\" и \"password\" перед выполнением запроса", () -> {
            loginRequest.setEmail("eve.holt@reqres.in");
            loginRequest.setPassword("password");
        });
        step("Делаем запрос на авторизацию пользователя", () -> {
            response = reqresClient.userLogin(loginRequest);
        });
        step("Статус код в ответе равен 200", () -> {
            response.statusCode(200);
        });
        step("Токен в ответе не пустой", () -> {
            LoginResponseModel loginResponseModel = response.extract().as(LoginResponseModel.class);
            assertNotNull(loginResponseModel.getToken());
        });
    }

}
