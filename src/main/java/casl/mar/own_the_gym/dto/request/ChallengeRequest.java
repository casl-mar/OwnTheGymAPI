package casl.mar.own_the_gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ChallengeRequest {

    @NotBlank
    private String title;

    private String description;

    private String imageUrl;

    private List<UUID> workoutIds = new ArrayList<>();
}
