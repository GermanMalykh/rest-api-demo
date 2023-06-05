package qa.demo.client;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import qa.demo.constants.Endpoints;
import qa.demo.model.UserRegistrationRequestModel;
import qa.demo.model.LoginRequestModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static qa.demo.specs.LoginSpecs.requestSpecification;
import static qa.demo.specs.LoginSpecs.responseSpecification;

public class ReqresClient {

    @Description("POST../api/user - Creates a session")
    public ValidatableResponse userLogin(LoginRequestModel loginRequest) {
        return given(requestSpecification)
                .contentType(JSON)
                .body(loginRequest)
                .post(Endpoints.LOGIN)
                .then()
                .spec(responseSpecification);
    }

    @Description("POST..api/users - Create a new user")
    public ValidatableResponse userRegistration(UserRegistrationRequestModel createUserRequestModel) {
        return given(requestSpecification)
                .contentType(JSON)
                .body(createUserRequestModel)
                .post(Endpoints.USER_REGISTRATION)
                .then()
                .spec(responseSpecification);
    }

}
