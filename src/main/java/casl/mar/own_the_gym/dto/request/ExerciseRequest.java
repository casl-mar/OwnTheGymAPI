package casl.mar.own_the_gym.dto.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonPropertyOrder({"title", "description", "videoUrl", "imageUrl"})
public class ExerciseRequest {

    @NotBlank
    private String title;

    private String description;

    private String videoUrl;

    private String imageUrl;
}
