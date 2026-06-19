package casl.mar.own_the_gym.service;

import casl.mar.own_the_gym.dto.request.LoginRequest;
import casl.mar.own_the_gym.dto.request.RegisterRequest;
import casl.mar.own_the_gym.dto.response.AuthResponse;
import casl.mar.own_the_gym.dto.response.UserResponse;
import casl.mar.own_the_gym.entity.Calendar;
import casl.mar.own_the_gym.entity.User;
import casl.mar.own_the_gym.exception.ConflictException;
import casl.mar.own_the_gym.repository.UserRepository;
import casl.mar.own_the_gym.security.JwtService;
import casl.mar.own_the_gym.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email is already registered");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username is already taken");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        Calendar calendar = Calendar.builder()
                .user(user)
                .build();
        user.setCalendar(calendar);

        userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);
        return buildAuthResponse(principal, user);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getEmail())
                .orElseThrow();

        return buildAuthResponse(principal, user);
    }

    public UserResponse getCurrentUser(UserPrincipal principal) {
        User user = userRepository.findByEmail(principal.getEmail())
                .orElseThrow();
        return UserResponse.from(user);
    }

    private AuthResponse buildAuthResponse(UserPrincipal principal, User user) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(principal))
                .tokenType("Bearer")
                .user(UserResponse.from(user))
                .build();
    }
}
