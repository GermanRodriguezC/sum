package com.sum.controller.advisor;

import com.sum.exception.CallHistoryException;
import com.sum.exception.ErrorResponse;
import com.sum.exception.RepositoryException;
import com.sum.exception.WireMockFeignClientException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class SumExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String EXCEPTION_MESSAGE = "Exception: ";

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
    log.error(EXCEPTION_MESSAGE, e);
    List<String> errors = new ArrayList<>();
    e.getConstraintViolations()
        .forEach(
            violation -> errors.add(violation.getPropertyPath() + ": " + violation.getMessage()));
    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.BAD_REQUEST.name(), errors.toString());
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(WireMockFeignClientException.class)
  public ResponseEntity<ErrorResponse> handleExternalException(WireMockFeignClientException ex) {
    log.error(EXCEPTION_MESSAGE, ex);
    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
  }

  @ExceptionHandler({RepositoryException.class, CallHistoryException.class})
  public ResponseEntity<ErrorResponse> handleException(RepositoryException e) {
    log.error(EXCEPTION_MESSAGE, e);
    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
    log.error(EXCEPTION_MESSAGE, e);
    ErrorResponse errorResponse =
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
    return ResponseEntity.internalServerError().body(errorResponse);
  }
}
