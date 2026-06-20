package casl.mar.own_the_gym.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"workoutDaysCount", "challengeDaysCount", "entries"})
public class StatisticsResponse {

    private long workoutDaysCount;
    private long challengeDaysCount;
    private List<StatisticsEntryResponse> entries;
}
