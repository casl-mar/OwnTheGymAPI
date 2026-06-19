package casl.mar.own_the_gym.controller;

import casl.mar.own_the_gym.config.OpenApiConfig;
import casl.mar.own_the_gym.dto.request.WorkoutRequest;
import casl.mar.own_the_gym.dto.response.WorkoutResponse;
import casl.mar.own_the_gym.security.UserPrincipal;
import casl.mar.own_the_gym.service.WorkoutService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody WorkoutRequest request
    ) {
        return workoutService.create(principal, request);
    }

    @GetMapping
    public List<WorkoutResponse> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        return workoutService.getAll(principal);
    }

    @GetMapping("/typical")
    public List<WorkoutResponse> getTypical(@AuthenticationPrincipal UserPrincipal principal) {
        return workoutService.getTypical(principal);
    }

    @GetMapping("/special")
    public List<WorkoutResponse> getSpecial(@AuthenticationPrincipal UserPrincipal principal) {
        return workoutService.getSpecial(principal);
    }

    @GetMapping("/{id}")
    public WorkoutResponse getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        return workoutService.getById(principal, id);
    }

    @PutMapping("/{id}")
    public WorkoutResponse update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody WorkoutRequest request
    ) {
        return workoutService.update(principal, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        workoutService.delete(principal, id);
    }
}
