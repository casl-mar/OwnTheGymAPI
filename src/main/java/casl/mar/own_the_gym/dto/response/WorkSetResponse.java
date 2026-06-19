package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.WorkSet;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "exerciseId", "exerciseTitle", "numberOfReps", "amountOfTime"})
public class WorkSetResponse {

    private UUID id;
    private UUID exerciseId;
    private String exerciseTitle;
    private Integer numberOfReps;
    private Integer amountOfTime;

    public static WorkSetResponse from(WorkSet workSet) {
        return WorkSetResponse.builder()
                .id(workSet.getId())
                .exerciseId(workSet.getExercise().getId())
                .exerciseTitle(workSet.getExercise().getTitle())
                .numberOfReps(workSet.getNumberOfReps())
                .amountOfTime(workSet.getAmountOfTime())
                .build();
    }
}
