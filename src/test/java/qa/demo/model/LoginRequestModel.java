package qa.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestModel {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

}
