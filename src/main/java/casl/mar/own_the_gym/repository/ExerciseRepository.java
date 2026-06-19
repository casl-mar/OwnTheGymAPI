package casl.mar.own_the_gym.repository;

import casl.mar.own_the_gym.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findAllByCreatorId(UUID creatorId);

    Optional<Exercise> findByIdAndCreatorId(UUID id, UUID creatorId);
}
