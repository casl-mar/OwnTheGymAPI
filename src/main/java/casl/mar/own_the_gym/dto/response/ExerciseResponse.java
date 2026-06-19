package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.Exercise;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "title", "description", "videoUrl", "imageUrl"})
public class ExerciseResponse {

    private UUID id;
    private String title;
    private String description;
    private String videoUrl;
    private String imageUrl;

    public static ExerciseResponse from(Exercise exercise) {
        return ExerciseResponse.builder()
                .id(exercise.getId())
                .title(exercise.getTitle())
                .description(exercise.getDescription())
                .videoUrl(exercise.getVideoUrl())
                .imageUrl(exercise.getImageUrl())
                .build();
    }
}
