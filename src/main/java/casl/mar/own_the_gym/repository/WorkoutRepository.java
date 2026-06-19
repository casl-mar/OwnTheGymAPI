package casl.mar.own_the_gym.repository;

import casl.mar.own_the_gym.entity.Workout;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findAllByCreatorId(UUID creatorId);

    List<Workout> findAllByCreatorIdAndType(UUID creatorId, WorkoutType type);

    Optional<Workout> findByIdAndCreatorId(UUID id, UUID creatorId);
}
