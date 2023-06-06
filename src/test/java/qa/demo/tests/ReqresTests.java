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
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static org.junit.jupiter.api.Assertions.*;
import static qa.demo.config.CustomAllureListener.withCustomTemplates;

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

    @DisplayName("WebShopTest")
    @Test
    void addToCartTest() {
        ValidatableResponse response =
                given()
                        .contentType(URLENC)
                        .filter(withCustomTemplates())
                        .log().all()
                        .cookie("NOPCOMMERCE.AUTH", "F25D8810A23985AC4E63A4ABD76822CEA2440BCC529D15D8C8D6D4078FB4E8C4F2B3DFC1A4E6AB86940D404D873A8A40B1B366C05B89A5479156B5E9BED4F52E7EA9E2D4531B1ACB74D4919D260C4C1A86B4873891C81EBFE48985CBAA62028017F37A5A82F6CD5FE62E731CEB5834DD3328E9CE99755266E38E5B707B5E5AF843B5D7BA7DEE1B8AF4B79DB6177496A1")
                        .formParam("product_attribute_72_5_18", "53")
                        .formParam("product_attribute_72_6_19", "54")
                        .formParam("product_attribute_72_3_20", "57")
                        .formParam("addtocart_72.EnteredQuantity", "1")
                        .when()
                        .post("https://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .log().all();
        response.statusCode(200);
        assertEquals(true, response.extract().path("success"));
    }

}
