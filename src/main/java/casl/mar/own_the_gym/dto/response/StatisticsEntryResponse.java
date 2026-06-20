package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.ChallengeDay;
import casl.mar.own_the_gym.entity.WorkoutDay;
import casl.mar.own_the_gym.entity.enums.ActivityType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "day", "type", "workout", "challenge"})
public class StatisticsEntryResponse {

    private UUID id;
    private LocalDateTime day;
    private ActivityType type;
    private WorkoutResponse workout;
    private ChallengeResponse challenge;

    public static StatisticsEntryResponse from(WorkoutDay workoutDay) {
        return StatisticsEntryResponse.builder()
                .id(workoutDay.getId())
                .day(workoutDay.getDay())
                .type(ActivityType.WORKOUT)
                .workout(WorkoutResponse.from(workoutDay.getWorkout()))
                .build();
    }

    public static StatisticsEntryResponse from(ChallengeDay challengeDay) {
        return StatisticsEntryResponse.builder()
                .id(challengeDay.getId())
                .day(challengeDay.getDay())
                .type(ActivityType.CHALLENGE)
                .challenge(ChallengeResponse.from(challengeDay.getChallenge()))
                .build();
    }
}
