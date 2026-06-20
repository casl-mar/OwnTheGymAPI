package casl.mar.own_the_gym.controller;

import casl.mar.own_the_gym.config.OpenApiConfig;
import casl.mar.own_the_gym.dto.request.CompleteActivityRequest;
import casl.mar.own_the_gym.dto.response.CalendarResponse;
import casl.mar.own_the_gym.dto.response.ChallengeDayResponse;
import casl.mar.own_the_gym.dto.response.StatisticsResponse;
import casl.mar.own_the_gym.dto.response.WorkoutDayResponse;
import casl.mar.own_the_gym.security.UserPrincipal;
import casl.mar.own_the_gym.service.CalendarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/workouts/{id}/complete")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutDayResponse completeWorkout(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @RequestBody(required = false) CompleteActivityRequest request
    ) {
        return calendarService.completeWorkout(principal, id, request);
    }

    @PostMapping("/challenges/{id}/complete")
    @ResponseStatus(HttpStatus.CREATED)
    public ChallengeDayResponse completeChallenge(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @RequestBody(required = false) CompleteActivityRequest request
    ) {
        return calendarService.completeChallenge(principal, id, request);
    }

    @GetMapping("/calendar/week")
    public CalendarResponse getCalendarByWeek(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return calendarService.getCalendarByWeek(principal, date);
    }

    @GetMapping("/calendar/month")
    public CalendarResponse getCalendarByMonth(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return calendarService.getCalendarByMonth(principal, year, month);
    }

    @GetMapping("/statistics")
    public StatisticsResponse getStatistics(@AuthenticationPrincipal UserPrincipal principal) {
        return calendarService.getStatistics(principal);
    }
}
