package casl.mar.own_the_gym.repository;

import casl.mar.own_the_gym.entity.WorkoutDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, UUID> {

    List<WorkoutDay> findByCalendarIdAndDayBetweenOrderByDayAsc(
            UUID calendarId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<WorkoutDay> findByCalendarIdOrderByDayDesc(UUID calendarId);

    long countByCalendarId(UUID calendarId);

    void deleteAllByWorkoutId(UUID workoutId);
}
