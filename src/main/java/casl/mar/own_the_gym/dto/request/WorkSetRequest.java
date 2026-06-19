package casl.mar.own_the_gym.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WorkSetRequest {

    @NotNull
    private UUID exerciseId;

    private Integer numberOfReps;

    private Integer amountOfTime;
}
