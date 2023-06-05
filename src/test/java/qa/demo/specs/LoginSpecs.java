package qa.demo.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.*;
import static qa.demo.helpers.CustomAllureListener.withCustomTemplates;

public class LoginSpecs {

    public static RequestSpecification requestSpecification = with()
            .filter(withCustomTemplates())
            .log().all();

    public static ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(ALL)
            .build();
}
