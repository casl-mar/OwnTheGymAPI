package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.Workout;
import casl.mar.own_the_gym.entity.enums.DifficultyLevel;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "title", "description", "imageUrl", "level", "type", "sets"})
public class WorkoutResponse {

    private UUID id;
    private String title;
    private String description;
    private String imageUrl;
    private DifficultyLevel level;
    private WorkoutType type;
    private List<WorkSetResponse> sets;

    public static WorkoutResponse from(Workout workout) {
        return WorkoutResponse.builder()
                .id(workout.getId())
                .title(workout.getTitle())
                .description(workout.getDescription())
                .imageUrl(workout.getImageUrl())
                .level(workout.getLevel())
                .type(workout.getType())
                .sets(workout.getSets().stream()
                        .map(WorkSetResponse::from)
                        .toList())
                .build();
    }
}
