package com.ebsco.edge.courses.service;

import com.ebsco.courses.domain.dto.Courselisting;
import com.ebsco.courses.domain.dto.Instructor;
import com.ebsco.courses.domain.dto.Instructors;
import com.ebsco.courses.domain.dto.LocateInstructor;
import com.ebsco.courses.domain.dto.RequestQueryParameters;
import com.ebsco.edge.courses.client.CourseClient;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseReservesService {

  private final CourseClient courseClient;
  @Value("${okapi_url}")
  private String okapiUrl;

  public String getCourseReservesByQuery(String tenant, String token, RequestQueryParameters requestQueryParameters) {
    log.info("Calling getCourseReservesByQuery with query: {}", requestQueryParameters.getQuery());
    ResponseEntity<String> reservesResponse = courseClient
        .getCourseByQuery(URI.create(okapiUrl), tenant, token, requestQueryParameters);
    log.info("Received response while getting getCourseReservesByQuery, status: {}",
        reservesResponse::getStatusCode);
    return reservesResponse.getBody();
  }

  public String getCourseReservesByInstanceId(String instanceId, String tenant, String token,
      RequestQueryParameters requestQueryParameters) {
    log.info("Calling getCourseReservesByInstanceId with id: {}", instanceId);
    ResponseEntity<String> reservesResponse = courseClient
        .getCourseById(URI.create(okapiUrl), instanceId, tenant, token, requestQueryParameters);
    log.info("Received response while getting getCourseReservesByInstanceId, status: {}",
        reservesResponse::getStatusCode);
    return reservesResponse.getBody();
  }

  public String getDepartments(String tenant, String token, RequestQueryParameters queryParameters) {
    log.info("Retrieving departments by query: {}", queryParameters.getQuery());
    ResponseEntity<String> departments = 
        courseClient.getDepartments(URI.create(okapiUrl), tenant, token, queryParameters);
    log.info("Received response while getting getDepartments, status: {}",
        departments::getStatusCode);
    return departments.getBody();
  }

  public Instructors getInstructors(String tenant, String token, RequestQueryParameters queryParameters) {
    RequestQueryParameters maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    log.info("Calling getInstructors");
    
    var courseListings = courseClient.getCourselistings(URI.create(okapiUrl), tenant, token, maxLimit);

    log.info("Received response while getting getInstructors, size: {}", 
        courseListings::getTotalRecords);
    
    List<LocateInstructor> instructors = courseListings.getCourseListings().stream()
        .map(Courselisting::getInstructorObjects)
        .flatMap(Collection::stream)
        .map(this::toLocateInstructor)
        .collect(Collectors.toList());

    var locateInstructors = paginateInstructors(instructors, queryParameters.getOffset(), queryParameters.getLimit());
    return new Instructors()
        .instructors(locateInstructors)
        .totalRecords(instructors.size());
  }

  private static List<LocateInstructor> paginateInstructors(
      List<LocateInstructor> instructors, Integer offset, Integer limit) {
    int size = instructors.size();
    if (size <= offset) {
      return Collections.emptyList();
    }
    limit = Math.min(limit + offset, size);
    return instructors.subList(offset, limit);
  }

  private LocateInstructor toLocateInstructor(Instructor instructor) {
    return new LocateInstructor()
        .id(instructor.getId())
        .name(instructor.getName());
  }
}
