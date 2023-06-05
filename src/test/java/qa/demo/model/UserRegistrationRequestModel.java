package qa.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRegistrationRequestModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("job")
    private String job;

}
