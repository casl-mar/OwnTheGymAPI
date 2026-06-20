package casl.mar.own_the_gym.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        (first, second) -> first
                ));

        return badRequest("Validation failed", errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String param = ex.getName();
        String message = resolveTypeMismatchMessage(ex);
        return badRequest(message, Map.of(param, message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        String message = "Required parameter '%s' is missing".formatted(ex.getParameterName());
        return badRequest(message, Map.of(ex.getParameterName(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getTargetType() == LocalDateTime.class) {
                return badRequest(
                        "Invalid datetime format. Expected: yyyy-MM-dd'T'HH:mm:ss",
                        Map.of("day", "Invalid datetime format. Expected: yyyy-MM-dd'T'HH:mm:ss")
                );
            }
            if (invalidFormat.getTargetType() == LocalDate.class) {
                return badRequest(
                        "Invalid date format. Expected: yyyy-MM-dd",
                        Map.of("day", "Invalid date format. Expected: yyyy-MM-dd")
                );
            }
        }

        if (cause instanceof DateTimeParseException) {
            return badRequest(
                    "Invalid date or time format",
                    Map.of("body", "Invalid date or time format")
            );
        }

        return badRequest("Invalid request body", Map.of("body", "Invalid request body"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message("Database constraint violation")
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return badRequest(ex.getMessage(), null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid email or password")
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    private ResponseEntity<ErrorResponse> badRequest(String message, Map<String, String> errors) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .timestamp(java.time.LocalDateTime.now())
                .errors(errors)
                .build());
    }

    private String resolveTypeMismatchMessage(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();

        if (requiredType == LocalDate.class) {
            return "Invalid date format. Expected: yyyy-MM-dd";
        }
        if (requiredType == LocalDateTime.class) {
            return "Invalid datetime format. Expected: yyyy-MM-dd'T'HH:mm:ss";
        }
        if (requiredType == UUID.class) {
            return "Invalid UUID format";
        }
        if (requiredType != null && requiredType.isEnum()) {
            return "Invalid value for parameter '%s'".formatted(ex.getName());
        }
        if (requiredType == Integer.class || requiredType == int.class) {
            return "Parameter '%s' must be a number".formatted(ex.getName());
        }

        return "Invalid value for parameter '%s'".formatted(ex.getName());
    }
}
