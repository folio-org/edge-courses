package com.ebsco.edgecourses.controller;

import com.ebsco.courses.rest.resource.CoursesApi;
import com.ebsco.edgecourses.service.CourseReservesService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Edge courses")
@Log4j2
@RestController
@RequiredArgsConstructor
public class CourseReservesController implements CoursesApi {

  private final CourseReservesService courseReservesService;

  @Override
  public ResponseEntity<String> getCoursesByQuery(String query, String tenant, String token) {
    return new ResponseEntity<>(courseReservesService.getCourseReservesByQuery(query, tenant, token), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> getCoursesByInstanceId(String instanceId, String tenant, String token) {
    return new ResponseEntity<>(courseReservesService.getCourseReservesByInstanceId(instanceId, tenant, token),
        HttpStatus.OK);
  }

}
