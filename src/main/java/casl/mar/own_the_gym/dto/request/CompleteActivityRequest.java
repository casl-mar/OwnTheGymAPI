package casl.mar.own_the_gym.dto.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({"day"})
public class CompleteActivityRequest {

    private LocalDateTime day;
}
