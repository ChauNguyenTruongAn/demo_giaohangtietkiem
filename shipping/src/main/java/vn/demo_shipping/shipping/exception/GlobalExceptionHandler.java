package vn.demo_shipping.shipping.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import vn.demo_shipping.shipping.dto.response.APIResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIResponse<String>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        APIResponse<String> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                "Exception: " + ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<String>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        APIResponse<String> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                "Exception: invalid request.", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
