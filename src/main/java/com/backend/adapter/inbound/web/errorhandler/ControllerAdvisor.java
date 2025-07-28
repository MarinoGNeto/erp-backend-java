package com.backend.adapter.inbound.web.errorhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ControllerAdvisor {

    private static final String ERROR = "error";
    private static final String ERRORS = "errors";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(Map.of(ERRORS, errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .toList();

        return ResponseEntity.badRequest().body(Map.of(ERRORS, errors));
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, TypeMismatchException.class })
    public ResponseEntity<Map<String, List<String>>> handleTypeMismatchExceptions(Exception ex) {
        String name;
        Object value;
        Class<?> requiredType;

        if (ex instanceof MethodArgumentTypeMismatchException mismatchEx) {
            name = mismatchEx.getName();
            value = mismatchEx.getValue();
            requiredType = mismatchEx.getRequiredType();
        } else if (ex instanceof TypeMismatchException mismatchEx) {
            name = mismatchEx.getPropertyName();
            value = mismatchEx.getValue();
            requiredType = mismatchEx.getRequiredType();
        } else {
            name = "parameter";
            value = "unknown";
            requiredType = null;
        }

        String expectedType = requiredType != null ? requiredType.getSimpleName() : "unknown";
        String message = String.format("%s: invalid value [%s]. Expected a valid %s.", name, value, expectedType);

        return ResponseEntity.badRequest().body(Map.of(ERRORS, List.of(message)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid request body.";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            String enumName = ife.getTargetType().getSimpleName();
            String invalidValue = String.valueOf(ife.getValue());
            String acceptedValues = String.join(", ",
                    Arrays.stream(ife.getTargetType().getEnumConstants())
                            .map(Object::toString)
                            .toList());

            message = String.format(
                    "Invalid value [%s] for enum [%s]. Accepted values: [%s]",
                    invalidValue, enumName, acceptedValues
            );
        }

        return ResponseEntity.badRequest().body(Map.of(ERROR, message));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(ERROR, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(ERROR, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(ERROR, "Unexpected error occurred."));
    }
}
