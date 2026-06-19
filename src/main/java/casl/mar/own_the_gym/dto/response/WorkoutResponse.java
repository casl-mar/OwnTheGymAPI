package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.Workout;
import casl.mar.own_the_gym.entity.enums.DifficultyLevel;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class WorkoutResponse {

    private UUID id;
    private String title;
    private String description;
    private DifficultyLevel level;
    private WorkoutType type;
    private List<WorkSetResponse> sets;

    public static WorkoutResponse from(Workout workout) {
        return WorkoutResponse.builder()
                .id(workout.getId())
                .title(workout.getTitle())
                .description(workout.getDescription())
                .level(workout.getLevel())
                .type(workout.getType())
                .sets(workout.getSets().stream()
                        .map(WorkSetResponse::from)
                        .toList())
                .build();
    }
}
