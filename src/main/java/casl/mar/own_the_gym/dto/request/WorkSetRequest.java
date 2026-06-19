package casl.mar.own_the_gym.dto.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@JsonPropertyOrder({"exerciseId", "numberOfReps", "amountOfTime"})
public class WorkSetRequest {

    @NotNull
    private UUID exerciseId;

    private Integer numberOfReps;

    private Integer amountOfTime;
}
