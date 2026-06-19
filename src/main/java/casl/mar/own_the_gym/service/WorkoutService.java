package casl.mar.own_the_gym.service;

import casl.mar.own_the_gym.dto.request.WorkSetRequest;
import casl.mar.own_the_gym.dto.request.WorkoutRequest;
import casl.mar.own_the_gym.dto.response.WorkoutResponse;
import casl.mar.own_the_gym.entity.Exercise;
import casl.mar.own_the_gym.entity.User;
import casl.mar.own_the_gym.entity.WorkSet;
import casl.mar.own_the_gym.entity.Workout;
import casl.mar.own_the_gym.exception.NotFoundException;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import casl.mar.own_the_gym.repository.UserRepository;
import casl.mar.own_the_gym.repository.WorkoutRepository;
import casl.mar.own_the_gym.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final ExerciseService exerciseService;
    private final UserRepository userRepository;

    @Transactional
    public WorkoutResponse create(UserPrincipal principal, WorkoutRequest request) {
        User creator = getUser(principal);

        Workout workout = Workout.builder()
                .creator(creator)
                .title(request.getTitle())
                .level(request.getLevel())
                .type(request.getType())
                .build();
        workout.setDescription(request.getDescription());
        applySets(workout, principal.getId(), request.getSets());

        return WorkoutResponse.from(workoutRepository.save(workout));
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponse> getAll(UserPrincipal principal) {
        return workoutRepository.findAllByCreatorId(principal.getId()).stream()
                .map(WorkoutResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponse> getTypical(UserPrincipal principal) {
        return getByType(principal, WorkoutType.TYPICAL);
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponse> getSpecial(UserPrincipal principal) {
        return getByType(principal, WorkoutType.SPECIAL);
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponse> getByType(UserPrincipal principal, WorkoutType type) {
        return workoutRepository.findAllByCreatorIdAndType(principal.getId(), type).stream()
                .map(WorkoutResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkoutResponse getById(UserPrincipal principal, UUID id) {
        return WorkoutResponse.from(findOwnedWorkout(principal.getId(), id));
    }

    @Transactional
    public WorkoutResponse update(UserPrincipal principal, UUID id, WorkoutRequest request) {
        Workout workout = findOwnedWorkout(principal.getId(), id);
        workout.setTitle(request.getTitle());
        workout.setDescription(request.getDescription());
        workout.setLevel(request.getLevel());
        workout.setType(request.getType());
        workout.getSets().clear();
        applySets(workout, principal.getId(), request.getSets());

        return WorkoutResponse.from(workout);
    }

    @Transactional
    public void delete(UserPrincipal principal, UUID id) {
        Workout workout = findOwnedWorkout(principal.getId(), id);
        workoutRepository.delete(workout);
    }

    private void applySets(Workout workout, UUID creatorId, List<WorkSetRequest> setRequests) {
        if (setRequests == null) {
            return;
        }

        for (WorkSetRequest setRequest : setRequests) {
            Exercise exercise = exerciseService.findOwnedExercise(creatorId, setRequest.getExerciseId());

            WorkSet workSet = WorkSet.builder()
                    .workout(workout)
                    .exercise(exercise)
                    .numberOfReps(setRequest.getNumberOfReps())
                    .amountOfTime(setRequest.getAmountOfTime())
                    .build();
            workout.getSets().add(workSet);
        }
    }

    private Workout findOwnedWorkout(UUID creatorId, UUID workoutId) {
        return workoutRepository.findByIdAndCreatorId(workoutId, creatorId)
                .orElseThrow(() -> new NotFoundException("Workout not found"));
    }

    private User getUser(UserPrincipal principal) {
        return userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
