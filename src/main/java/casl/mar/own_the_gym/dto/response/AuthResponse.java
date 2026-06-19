package casl.mar.own_the_gym.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"accessToken", "tokenType", "user"})
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private UserResponse user;
}
