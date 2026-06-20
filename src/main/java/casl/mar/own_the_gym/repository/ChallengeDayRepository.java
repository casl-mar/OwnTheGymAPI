package casl.mar.own_the_gym.repository;

import casl.mar.own_the_gym.entity.ChallengeDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ChallengeDayRepository extends JpaRepository<ChallengeDay, UUID> {

    List<ChallengeDay> findByCalendarIdAndDayBetweenOrderByDayAsc(
            UUID calendarId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<ChallengeDay> findByCalendarIdOrderByDayDesc(UUID calendarId);

    long countByCalendarId(UUID calendarId);
}
