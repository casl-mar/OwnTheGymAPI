package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.ChallengeDay;
import casl.mar.own_the_gym.entity.WorkoutDay;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "day", "workout"})
public class WorkoutDayResponse {

    private UUID id;
    private LocalDateTime day;
    private WorkoutResponse workout;

    public static WorkoutDayResponse from(WorkoutDay workoutDay) {
        return WorkoutDayResponse.builder()
                .id(workoutDay.getId())
                .day(workoutDay.getDay())
                .workout(WorkoutResponse.from(workoutDay.getWorkout()))
                .build();
    }
}
