package qa.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRegistrationResponseModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("job")
    private String job;

    @JsonProperty("id")
    private String id;

    @JsonProperty("createdAt")
    private String createdAt;

}
