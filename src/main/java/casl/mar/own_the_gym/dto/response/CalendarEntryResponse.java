package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.enums.ActivityType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonPropertyOrder({"day", "type"})
public class CalendarEntryResponse {

    private LocalDate day;
    private ActivityType type;
}
