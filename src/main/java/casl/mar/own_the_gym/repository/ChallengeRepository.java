package casl.mar.own_the_gym.repository;

import casl.mar.own_the_gym.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {

    List<Challenge> findAllByCreatorId(UUID creatorId);

    Optional<Challenge> findByIdAndCreatorId(UUID id, UUID creatorId);
}
