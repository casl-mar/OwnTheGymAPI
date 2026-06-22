package casl.mar.own_the_gym.service;

import casl.mar.own_the_gym.dto.request.ChallengeRequest;
import casl.mar.own_the_gym.dto.response.ChallengeResponse;
import casl.mar.own_the_gym.entity.Challenge;
import casl.mar.own_the_gym.entity.ChallengeWorkout;
import casl.mar.own_the_gym.entity.User;
import casl.mar.own_the_gym.entity.Workout;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import casl.mar.own_the_gym.exception.BadRequestException;
import casl.mar.own_the_gym.exception.NotFoundException;
import casl.mar.own_the_gym.repository.ChallengeDayRepository;
import casl.mar.own_the_gym.repository.ChallengeRepository;
import casl.mar.own_the_gym.repository.UserRepository;
import casl.mar.own_the_gym.repository.WorkoutRepository;
import casl.mar.own_the_gym.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeDayRepository challengeDayRepository;
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChallengeResponse create(UserPrincipal principal, ChallengeRequest request) {
        User creator = getUser(principal);

        Challenge challenge = Challenge.builder()
                .creator(creator)
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        applyWorkouts(challenge, principal.getId(), request.getWorkoutIds());

        return ChallengeResponse.from(challengeRepository.save(challenge));
    }

    @Transactional(readOnly = true)
    public List<ChallengeResponse> getAll(UserPrincipal principal) {
        return challengeRepository.findAllByCreatorId(principal.getId()).stream()
                .map(ChallengeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChallengeResponse getById(UserPrincipal principal, UUID id) {
        return ChallengeResponse.from(findOwnedChallenge(principal.getId(), id));
    }

    @Transactional
    public ChallengeResponse update(UserPrincipal principal, UUID id, ChallengeRequest request) {
        Challenge challenge = findOwnedChallenge(principal.getId(), id);
        challenge.setTitle(request.getTitle());
        challenge.setDescription(request.getDescription());
        challenge.setImageUrl(request.getImageUrl());
        challenge.getChallengeWorkouts().clear();
        applyWorkouts(challenge, principal.getId(), request.getWorkoutIds());

        return ChallengeResponse.from(challenge);
    }

    @Transactional
    public void delete(UserPrincipal principal, UUID id) {
        Challenge challenge = findOwnedChallenge(principal.getId(), id);
        challengeDayRepository.deleteAllByChallengeId(challenge.getId());
        challengeRepository.delete(challenge);
    }

    private void applyWorkouts(Challenge challenge, UUID creatorId, List<UUID> workoutIds) {
        if (workoutIds == null || workoutIds.isEmpty()) {
            throw new BadRequestException("Challenge must have at least one workout");
        }

        Set<UUID> uniqueIds = new HashSet<>();
        for (UUID workoutId : workoutIds) {
            if (!uniqueIds.add(workoutId)) {
                throw new BadRequestException("Duplicate workout in challenge");
            }

            Workout workout = workoutRepository.findByIdAndCreatorId(workoutId, creatorId)
                    .orElseThrow(() -> new NotFoundException("Workout not found"));

            if (workout.getType() != WorkoutType.SPECIAL) {
                throw new BadRequestException("Only SPECIAL workouts can be added to a challenge");
            }

            ChallengeWorkout challengeWorkout = ChallengeWorkout.builder()
                    .challenge(challenge)
                    .workout(workout)
                    .build();
            challenge.getChallengeWorkouts().add(challengeWorkout);
        }
    }

    private Challenge findOwnedChallenge(UUID creatorId, UUID challengeId) {
        return challengeRepository.findByIdAndCreatorId(challengeId, creatorId)
                .orElseThrow(() -> new NotFoundException("Challenge not found"));
    }

    private User getUser(UserPrincipal principal) {
        return userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
