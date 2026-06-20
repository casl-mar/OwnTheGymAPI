package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.enums.CalendarPeriod;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"period", "year", "month", "startDate", "endDate", "entries"})
public class CalendarResponse {

    private CalendarPeriod period;
    private Integer year;
    private Integer month;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CalendarEntryResponse> entries;
}
