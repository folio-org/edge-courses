package com.ebsco.edge.courses.controller;

import com.ebsco.courses.domain.dto.Instructors;
import com.ebsco.courses.domain.dto.RequestQueryParameters;
import com.ebsco.courses.rest.resource.CoursesApi;
import com.ebsco.edge.courses.service.CourseReservesService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    String coursesResponse = 
        courseReservesService.getCourseReservesByQuery(xOkapiTenant, xOkapiToken, queryParameters);
    return ResponseEntity.ok(coursesResponse);
  }

  @Override
  public ResponseEntity<String> getCoursesByInstanceId(String instanceId, String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters queryParameters) {
    String reservesResponse = courseReservesService
        .getCourseReservesByInstanceId(instanceId, xOkapiTenant, xOkapiToken, queryParameters);
    return ResponseEntity.ok(reservesResponse);
  }

  @Override
  public ResponseEntity<String> getDepartments(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters requestQueryParameters) {
    var departments = courseReservesService.getDepartments(xOkapiTenant, xOkapiToken,
        requestQueryParameters);
    return ResponseEntity.ok(departments);
  }

  @Override
  public ResponseEntity<Instructors> getInstructors(String tenant, String token, RequestQueryParameters queryParams) {
    Instructors instructors = courseReservesService.getInstructors(tenant, token, queryParams);
    return ResponseEntity.ok(instructors);
  }

}
