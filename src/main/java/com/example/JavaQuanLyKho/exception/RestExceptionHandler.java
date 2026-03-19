package com.example.JavaQuanLyKho.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND",
                "Resource not found",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        String code = "CONFLICT_DATA";
        String message = "Request conflicts with existing data";
        if (contains(ex, "CONFLICT_DUPLICATE_CODE")) {
            code = "CONFLICT_DUPLICATE_CODE";
            message = "Duplicate code/sku/barcode";
        }
        return build(HttpStatus.CONFLICT, code, message, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        if (message.isBlank()) {
            message = "Validation failed";
        }
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "VALIDATION_FAILED", message, request.getRequestURI());
    }

    @ExceptionHandler({BindException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiErrorResponse> handleRequestValidation(Exception ex, HttpServletRequest request) {
        return build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "VALIDATION_FAILED",
                "Invalid request payload",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "VALIDATION_FAILED",
                ex.getMessage() != null ? ex.getMessage() : "Invalid request data",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidStateTransition(
            InvalidStateTransitionException ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "INVALID_STATE_TRANSITION",
                ex.getMessage() != null ? ex.getMessage() : "Invalid state transition",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(OutboundStockNotEnoughException.class)
    public ResponseEntity<ApiErrorResponse> handleOutboundStockNotEnough(
            OutboundStockNotEnoughException ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.CONFLICT,
                "OUTBOUND_STOCK_NOT_ENOUGH",
                ex.getMessage() != null ? ex.getMessage() : "Not enough stock for outbound",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                request.getRequestURI()
        );
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String code, String message, String path) {
        ApiErrorResponse body = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                path
        );
        return ResponseEntity.status(status).body(body);
    }

    private boolean contains(Exception ex, String token) {
        if (ex == null) {
            return false;
        }
        if (ex.getMessage() != null && ex.getMessage().contains(token)) {
            return true;
        }
        Throwable cause = ex.getCause();
        return cause != null && cause.getMessage() != null && cause.getMessage().contains(token);
    }
}
