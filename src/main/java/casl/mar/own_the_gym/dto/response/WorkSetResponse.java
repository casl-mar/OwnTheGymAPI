package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.WorkSet;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
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
