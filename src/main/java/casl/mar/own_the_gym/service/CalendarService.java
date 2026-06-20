package casl.mar.own_the_gym.service;

import casl.mar.own_the_gym.dto.request.CompleteActivityRequest;
import casl.mar.own_the_gym.dto.response.CalendarEntryResponse;
import casl.mar.own_the_gym.dto.response.CalendarResponse;
import casl.mar.own_the_gym.dto.response.ChallengeDayResponse;
import casl.mar.own_the_gym.dto.response.StatisticsEntryResponse;
import casl.mar.own_the_gym.dto.response.StatisticsResponse;
import casl.mar.own_the_gym.dto.response.WorkoutDayResponse;
import casl.mar.own_the_gym.entity.Calendar;
import casl.mar.own_the_gym.entity.Challenge;
import casl.mar.own_the_gym.entity.ChallengeDay;
import casl.mar.own_the_gym.entity.Workout;
import casl.mar.own_the_gym.entity.WorkoutDay;
import casl.mar.own_the_gym.entity.enums.ActivityType;
import casl.mar.own_the_gym.entity.enums.CalendarPeriod;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import casl.mar.own_the_gym.exception.BadRequestException;
import casl.mar.own_the_gym.exception.NotFoundException;
import casl.mar.own_the_gym.repository.CalendarRepository;
import casl.mar.own_the_gym.repository.ChallengeDayRepository;
import casl.mar.own_the_gym.repository.ChallengeRepository;
import casl.mar.own_the_gym.repository.WorkoutDayRepository;
import casl.mar.own_the_gym.repository.WorkoutRepository;
import casl.mar.own_the_gym.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final WorkoutRepository workoutRepository;
    private final ChallengeRepository challengeRepository;
    private final WorkoutDayRepository workoutDayRepository;
    private final ChallengeDayRepository challengeDayRepository;

    @Transactional
    public WorkoutDayResponse completeWorkout(
            UserPrincipal principal,
            UUID workoutId,
            CompleteActivityRequest request
    ) {
        Workout workout = workoutRepository.findByIdAndCreatorId(workoutId, principal.getId())
                .orElseThrow(() -> new NotFoundException("Workout not found"));

        if (workout.getType() != WorkoutType.TYPICAL) {
            throw new BadRequestException("Only TYPICAL workouts can be completed");
        }

        Calendar calendar = getUserCalendar(principal.getId());
        WorkoutDay workoutDay = WorkoutDay.builder()
                .calendar(calendar)
                .workout(workout)
                .day(resolveDay(request))
                .build();

        return WorkoutDayResponse.from(workoutDayRepository.save(workoutDay));
    }

    @Transactional
    public ChallengeDayResponse completeChallenge(
            UserPrincipal principal,
            UUID challengeId,
            CompleteActivityRequest request
    ) {
        Challenge challenge = challengeRepository.findByIdAndCreatorId(challengeId, principal.getId())
                .orElseThrow(() -> new NotFoundException("Challenge not found"));

        Calendar calendar = getUserCalendar(principal.getId());
        ChallengeDay challengeDay = ChallengeDay.builder()
                .calendar(calendar)
                .challenge(challenge)
                .day(resolveDay(request))
                .build();

        return ChallengeDayResponse.from(challengeDayRepository.save(challengeDay));
    }

    @Transactional(readOnly = true)
    public CalendarResponse getCalendarByWeek(UserPrincipal principal, LocalDate date) {
        LocalDate startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = startDate.plusDays(6);

        return buildCalendarResponse(
                principal,
                CalendarPeriod.WEEK,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX),
                startDate,
                endDate,
                null,
                null
        );
    }

    @Transactional(readOnly = true)
    public CalendarResponse getCalendarByMonth(UserPrincipal principal, int year, int month) {
        if (month < 1 || month > 12) {
            throw new BadRequestException("Month must be between 1 and 12");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return buildCalendarResponse(
                principal,
                CalendarPeriod.MONTH,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX),
                startDate,
                endDate,
                year,
                month
        );
    }

    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics(UserPrincipal principal) {
        Calendar calendar = getUserCalendar(principal.getId());

        List<WorkoutDay> workoutDays = workoutDayRepository.findByCalendarIdOrderByDayDesc(calendar.getId());
        List<ChallengeDay> challengeDays = challengeDayRepository.findByCalendarIdOrderByDayDesc(calendar.getId());

        List<StatisticsEntryResponse> entries = new ArrayList<>();
        workoutDays.forEach(workoutDay -> entries.add(StatisticsEntryResponse.from(workoutDay)));
        challengeDays.forEach(challengeDay -> entries.add(StatisticsEntryResponse.from(challengeDay)));

        entries.sort(Comparator.comparing(StatisticsEntryResponse::getDay).reversed());

        return StatisticsResponse.builder()
                .workoutDaysCount(workoutDayRepository.countByCalendarId(calendar.getId()))
                .challengeDaysCount(challengeDayRepository.countByCalendarId(calendar.getId()))
                .entries(entries)
                .build();
    }

    private CalendarResponse buildCalendarResponse(
            UserPrincipal principal,
            CalendarPeriod period,
            LocalDateTime start,
            LocalDateTime end,
            LocalDate startDate,
            LocalDate endDate,
            Integer year,
            Integer month
    ) {
        Calendar calendar = getUserCalendar(principal.getId());

        List<WorkoutDay> workoutDays = workoutDayRepository.findByCalendarIdAndDayBetweenOrderByDayAsc(
                calendar.getId(),
                start,
                end
        );
        List<ChallengeDay> challengeDays = challengeDayRepository.findByCalendarIdAndDayBetweenOrderByDayAsc(
                calendar.getId(),
                start,
                end
        );

        Map<LocalDate, ActivityType> entriesByDay = new LinkedHashMap<>();

        for (WorkoutDay workoutDay : workoutDays) {
            LocalDate day = workoutDay.getDay().toLocalDate();
            entriesByDay.putIfAbsent(day, ActivityType.WORKOUT);
        }

        for (ChallengeDay challengeDay : challengeDays) {
            LocalDate day = challengeDay.getDay().toLocalDate();
            entriesByDay.put(day, ActivityType.CHALLENGE);
        }

        List<CalendarEntryResponse> entries = entriesByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> CalendarEntryResponse.builder()
                        .day(entry.getKey())
                        .type(entry.getValue())
                        .build())
                .toList();

        return CalendarResponse.builder()
                .period(period)
                .year(year)
                .month(month)
                .startDate(startDate)
                .endDate(endDate)
                .entries(entries)
                .build();
    }

    private Calendar getUserCalendar(UUID userId) {
        return calendarRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Calendar not found"));
    }

    private LocalDateTime resolveDay(CompleteActivityRequest request) {
        if (request == null || request.getDay() == null) {
            return LocalDateTime.now();
        }
        return request.getDay();
    }
}
