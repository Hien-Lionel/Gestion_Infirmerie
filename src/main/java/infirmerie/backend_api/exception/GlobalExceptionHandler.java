package infirmerie.backend_api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(404).body(
            ApiError.builder()
                    .status(404)
                    .message(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .errors(List.of())
                    .build()
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + " : " + e.getDefaultMessage())
            .toList();

    return ResponseEntity.status(400).body(
            ApiError.builder()
                    .status(400)
                    .message("Erreur de validation")
                    .timestamp(LocalDateTime.now())
                    .errors(errors)
                    .build()
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex) {
    return ResponseEntity.status(500).body(
            ApiError.builder()
                    .status(500)
                    .message("Erreur interne du serveur")
                    .timestamp(LocalDateTime.now())
                    .errors(List.of(ex.getMessage()))
                    .build()
    );
  }
}