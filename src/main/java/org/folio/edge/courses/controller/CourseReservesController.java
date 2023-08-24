package org.folio.edge.courses.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.courses.domain.dto.Instructors;
import org.folio.courses.domain.dto.RequestQueryParameters;
import org.folio.courses.rest.resource.CoursesApi;
import org.folio.edge.courses.service.CourseReservesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Edge courses")
@Log4j2
@RestController
@RequiredArgsConstructor
public class CourseReservesController implements CoursesApi {

  private final CourseReservesService courseReservesService;

  @Override
  public ResponseEntity<String> getCoursesByQuery(String xOkapiTenant, String xOkapiToken,
    RequestQueryParameters queryParameters) {
    var courses = courseReservesService.getCoursesByQuery(queryParameters);
    return ResponseEntity.ok(courses);
  }

  @Override
  public ResponseEntity<String> getReservesByQuery(String xOkapiTenant, String xOkapiToken,
    RequestQueryParameters queryParameters) {
    var reserves = courseReservesService.getReservesByQuery(queryParameters);
    return ResponseEntity.ok(reserves);
  }

  @Override
  public ResponseEntity<String> getReservesByInstanceId(String instanceId, String xOkapiTenant, String xOkapiToken,
    RequestQueryParameters queryParameters) {
    var reserves = courseReservesService.getReservesByInstanceId(instanceId, queryParameters);
    return ResponseEntity.ok(reserves);
  }

  @Override
  public ResponseEntity<String> getDepartments(String xOkapiTenant, String xOkapiToken,
    RequestQueryParameters requestQueryParameters) {
    var departments = courseReservesService.getDepartments(requestQueryParameters);
    return ResponseEntity.ok(departments);
  }

  @Override
  public ResponseEntity<Instructors> getInstructors(String tenant, String token, RequestQueryParameters queryParams,
    String sortBy) {
    var instructors = courseReservesService.getInstructors(queryParams, sortBy);
    return ResponseEntity.ok(instructors);
  }

}
