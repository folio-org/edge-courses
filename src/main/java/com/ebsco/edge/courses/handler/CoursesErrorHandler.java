package com.ebsco.edge.courses.handler;

import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class CoursesErrorHandler {

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<String> handleFeignException(FeignException exception) {
    String properErrorMessage = exception.contentUTF8();
    log.error("Error occurred during service chain call, {}", properErrorMessage);
    return ResponseEntity.status(exception.status())
        .contentType(MediaType.APPLICATION_JSON)
        .body(properErrorMessage);
  }
}
