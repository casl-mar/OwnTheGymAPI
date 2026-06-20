package casl.mar.own_the_gym.repository;

import casl.mar.own_the_gym.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CalendarRepository extends JpaRepository<Calendar, UUID> {

    Optional<Calendar> findByUserId(UUID userId);
}
