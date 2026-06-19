package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.Challenge;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "title", "description", "imageUrl", "workouts"})
public class ChallengeResponse {

    private UUID id;
    private String title;
    private String description;
    private String imageUrl;
    private List<WorkoutResponse> workouts;

    public static ChallengeResponse from(Challenge challenge) {
        return ChallengeResponse.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .imageUrl(challenge.getImageUrl())
                .workouts(challenge.getChallengeWorkouts().stream()
                        .map(challengeWorkout -> WorkoutResponse.from(challengeWorkout.getWorkout()))
                        .toList())
                .build();
    }
}
