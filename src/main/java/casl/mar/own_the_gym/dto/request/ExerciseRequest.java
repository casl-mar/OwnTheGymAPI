package casl.mar.own_the_gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExerciseRequest {

    @NotBlank
    private String title;

    private String description;

    private String videoUrl;
}
