package casl.mar.own_the_gym.controller;

import casl.mar.own_the_gym.config.OpenApiConfig;
import casl.mar.own_the_gym.dto.request.ExerciseRequest;
import casl.mar.own_the_gym.dto.response.ExerciseResponse;
import casl.mar.own_the_gym.security.UserPrincipal;
import casl.mar.own_the_gym.service.ExerciseService;
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
@RequestMapping("/exercises")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseResponse create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ExerciseRequest request
    ) {
        return exerciseService.create(principal, request);
    }

    @GetMapping
    public List<ExerciseResponse> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        return exerciseService.getAll(principal);
    }

    @GetMapping("/{id}")
    public ExerciseResponse getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        return exerciseService.getById(principal, id);
    }

    @PutMapping("/{id}")
    public ExerciseResponse update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody ExerciseRequest request
    ) {
        return exerciseService.update(principal, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        exerciseService.delete(principal, id);
    }
}
