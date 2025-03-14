package vn.demo_shipping.shipping.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<APIResponse<Object>> handleHttpClientErrorException(
            HttpClientErrorException ex) {
        APIResponse<Object> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), "Exception ghtk",
                ex.getMessage(), LocalDateTime.now());

        ObjectMapper objectMapper = new ObjectMapper();
        String dataString = ex.getMessage();
        try {
            Object dataJson = objectMapper.readTree(dataString);
            response.setData(dataJson);
        } catch (JsonProcessingException e) {
            response.setData(dataString);
        }

        return ResponseEntity.ok(response);
    }

}
