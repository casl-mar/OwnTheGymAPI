package casl.mar.own_the_gym.dto.request;

import casl.mar.own_the_gym.entity.enums.DifficultyLevel;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonPropertyOrder({"title", "description", "imageUrl", "level", "type", "sets"})
public class WorkoutRequest {

    @NotBlank
    private String title;

    private String description;

    private String imageUrl;

    @NotNull
    private DifficultyLevel level;

    @NotNull
    private WorkoutType type;

    @Valid
    private List<WorkSetRequest> sets = new ArrayList<>();
}
