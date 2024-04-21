package com.johnny.libmgtbackend.exception;

import com.johnny.libmgtbackend.dtos.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleException(Throwable throwable, HttpServletRequest request) {

        if (throwable instanceof ModelNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(throwable.getMessage()));
        }

        if (throwable instanceof DataIntegrityViolationException) {
            return ResponseEntity.unprocessableEntity().body(new ErrorDto(throwable.getMessage()));
        }

        if (throwable instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto("Authentication failed"));
        }

        if (throwable instanceof MethodArgumentNotValidException) {
            var errorDto = new ErrorDto("Invalid field value");

            errorDto.fieldErrors = ((MethodArgumentNotValidException) throwable).getFieldErrors().stream().collect(Collectors.toMap(
                    FieldError::getField,
                    item -> item.getDefaultMessage() == null ? "" : item.getDefaultMessage()
            ));

            return ResponseEntity.unprocessableEntity().body(errorDto);
        }

        if (throwable instanceof ConstraintViolationException) {
            var errorDto = new ErrorDto("Constraint violated");

            errorDto.fieldErrors = ((ConstraintViolationException) throwable).getConstraintViolations().stream().collect(Collectors.toMap(
                    item -> item.getPropertyPath().toString(),
                    ConstraintViolation::getMessage
            ));

            return ResponseEntity.unprocessableEntity().body(errorDto);
        }

        return ResponseEntity.internalServerError().body(new ErrorDto( "Unhandled Exception: " + throwable.getMessage()));
    }
}
