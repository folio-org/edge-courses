package com.ebsco.edgecourses.controller;

import com.ebsco.courses.rest.resource.CoursesApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api(tags = "Edge courses")
@Log4j2
@RestController
@RequiredArgsConstructor
public class CourseReservesController implements CoursesApi {

  private static final String RESERVES_RESPONSE_MOCK_PATH = "src/main/resources/mockData/reserves_response.json";
  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<String> getCourses(String query) {
    try {
      return new ResponseEntity<>(
          objectMapper.readValue(Paths.get(RESERVES_RESPONSE_MOCK_PATH).toFile(), String.class),
          HttpStatus.OK);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @Override
  public ResponseEntity<String> getCoursesByInstanceId(String instanceId) {
    try {
      return new ResponseEntity<>(
          objectMapper.readValue(Paths.get(RESERVES_RESPONSE_MOCK_PATH).toFile(), String.class),
          HttpStatus.OK);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

}
