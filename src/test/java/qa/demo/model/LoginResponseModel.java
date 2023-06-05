package qa.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponseModel {

    @JsonProperty("token")
    private String token;

}
