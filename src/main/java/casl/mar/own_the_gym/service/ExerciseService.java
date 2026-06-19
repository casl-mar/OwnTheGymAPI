package casl.mar.own_the_gym.service;

import casl.mar.own_the_gym.dto.request.ExerciseRequest;
import casl.mar.own_the_gym.dto.response.ExerciseResponse;
import casl.mar.own_the_gym.entity.Exercise;
import casl.mar.own_the_gym.entity.User;
import casl.mar.own_the_gym.exception.NotFoundException;
import casl.mar.own_the_gym.repository.ExerciseRepository;
import casl.mar.own_the_gym.repository.UserRepository;
import casl.mar.own_the_gym.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional
    public ExerciseResponse create(UserPrincipal principal, ExerciseRequest request) {
        User creator = getUser(principal);

        Exercise exercise = Exercise.builder()
                .creator(creator)
                .title(request.getTitle())
                .description(request.getDescription())
                .videoUrl(request.getVideoUrl())
                .imageUrl(request.getImageUrl())
                .build();

        return ExerciseResponse.from(exerciseRepository.save(exercise));
    }

    @Transactional(readOnly = true)
    public List<ExerciseResponse> getAll(UserPrincipal principal) {
        return exerciseRepository.findAllByCreatorId(principal.getId()).stream()
                .map(ExerciseResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExerciseResponse getById(UserPrincipal principal, UUID id) {
        return ExerciseResponse.from(findOwnedExercise(principal.getId(), id));
    }

    @Transactional
    public ExerciseResponse update(UserPrincipal principal, UUID id, ExerciseRequest request) {
        Exercise exercise = findOwnedExercise(principal.getId(), id);
        exercise.setTitle(request.getTitle());
        exercise.setDescription(request.getDescription());
        exercise.setVideoUrl(request.getVideoUrl());
        exercise.setImageUrl(request.getImageUrl());
        return ExerciseResponse.from(exercise);
    }

    @Transactional
    public void delete(UserPrincipal principal, UUID id) {
        Exercise exercise = findOwnedExercise(principal.getId(), id);
        exerciseRepository.delete(exercise);
    }

    Exercise findOwnedExercise(UUID creatorId, UUID exerciseId) {
        return exerciseRepository.findByIdAndCreatorId(exerciseId, creatorId)
                .orElseThrow(() -> new NotFoundException("Exercise not found"));
    }

    private User getUser(UserPrincipal principal) {
        return userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
