package casl.mar.own_the_gym.controller;

import casl.mar.own_the_gym.config.OpenApiConfig;
import casl.mar.own_the_gym.dto.request.ChallengeRequest;
import casl.mar.own_the_gym.dto.response.ChallengeResponse;
import casl.mar.own_the_gym.security.UserPrincipal;
import casl.mar.own_the_gym.service.ChallengeService;
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
@RequestMapping("/challenges")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChallengeResponse create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChallengeRequest request
    ) {
        return challengeService.create(principal, request);
    }

    @GetMapping
    public List<ChallengeResponse> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        return challengeService.getAll(principal);
    }

    @GetMapping("/{id}")
    public ChallengeResponse getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        return challengeService.getById(principal, id);
    }

    @PutMapping("/{id}")
    public ChallengeResponse update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody ChallengeRequest request
    ) {
        return challengeService.update(principal, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id
    ) {
        challengeService.delete(principal, id);
    }
}
