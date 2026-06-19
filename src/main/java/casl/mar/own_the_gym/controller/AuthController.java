package casl.mar.own_the_gym.controller;

import casl.mar.own_the_gym.config.OpenApiConfig;
import casl.mar.own_the_gym.dto.request.LoginRequest;
import casl.mar.own_the_gym.dto.request.RegisterRequest;
import casl.mar.own_the_gym.dto.response.AuthResponse;
import casl.mar.own_the_gym.dto.response.UserResponse;
import casl.mar.own_the_gym.security.UserPrincipal;
import casl.mar.own_the_gym.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
    public UserResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        return authService.getCurrentUser(principal);
    }
}
