package org.folio.edge.courses.service;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import com.google.common.base.Splitter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.courses.domain.dto.Courselisting;
import org.folio.courses.domain.dto.Instructor;
import org.folio.courses.domain.dto.InstructorMinimal;
import org.folio.courses.domain.dto.Instructors;
import org.folio.courses.domain.dto.RequestQueryParameters;
import org.folio.edge.courses.client.CourseClient;
import org.folio.edge.courses.model.dto.SortDirection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseReservesService {

  private static final String NAME_FIELD = "name";
  private static final String SLASH_SEPARATOR = "/";
  private final CourseClient courseClient;
  @Value("${okapi_url}")
  private String okapiUrl;

  public String getCoursesByQuery(RequestQueryParameters requestQueryParameters) {
    log.info("Calling getCoursesByQuery with query: {}", requestQueryParameters.getQuery());
    var courses = courseClient.getCourseByQuery(requestQueryParameters);
    log.info("Received response while getting getCoursesByQuery, status: {}",
      courses::getStatusCode);
    return courses.getBody();
  }

  public String getReservesByQuery(RequestQueryParameters requestQueryParameters) {
    log.info("Calling getReservesByQuery with query: {}", requestQueryParameters.getQuery());
    var reserves = courseClient.getReservesByQuery(requestQueryParameters);
    log.info("Received response while getting getReservesByQuery, status: {}",
      reserves::getStatusCode);
    return reserves.getBody();
  }

  public String getReservesByInstanceId(String instanceId, RequestQueryParameters requestQueryParameters) {
    log.info("Calling getCoursesByInstanceId with id: {}", instanceId);
    var reserves = courseClient.getReservesByInstanceId(instanceId, requestQueryParameters);
    log.info("Received response while getting getCoursesByInstanceId, status: {}",
      reserves::getStatusCode);
    return reserves.getBody();
  }

  public String getDepartments(RequestQueryParameters queryParameters) {
    log.info("Retrieving departments by query: {}", queryParameters.getQuery());
    var departments = courseClient.getDepartments(queryParameters);
    log.info("Received response while getting getDepartments, status: {}",
      departments::getStatusCode);
    return departments.getBody();
  }

  public Instructors getInstructors(RequestQueryParameters queryParameters, String sortBy) {
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    log.info("Calling getInstructors");

    var courseListings = courseClient.getCourselistings(maxLimit);

    log.info("Received response while getting getInstructors, size: {}", courseListings::getTotalRecords);
    var courseInstructors = courseListings.getCourseListings().stream()
      .map(Courselisting::getInstructorObjects)
      .flatMap(Collection::stream)
      .map(this::toInstructorMinimal)
      .toList();

    if (isNoneBlank(sortBy)) {
      courseInstructors = courseInstructors.stream()
        .sorted(getInstructorMinimalComparator(sortBy))
        .toList();
    }

    var instructors = paginateInstructors(courseInstructors, queryParameters.getOffset(), queryParameters.getLimit());
    return new Instructors(instructors, courseInstructors.size());
  }

  private List<InstructorMinimal> paginateInstructors(
    List<InstructorMinimal> instructors, Integer offset, Integer limit) {
    int size = instructors.size();
    if (size <= offset) {
      return Collections.emptyList();
    }
    limit = Math.min(limit + offset, size);
    return instructors.subList(offset, limit);
  }

  private InstructorMinimal toInstructorMinimal(Instructor instructor) {
    return new InstructorMinimal(instructor.getName().trim())
      .id(instructor.getId());
  }

  private Comparator<InstructorMinimal> getInstructorMinimalComparator(String sortBy) {
    var sortParts = Splitter.on(SLASH_SEPARATOR)
      .trimResults()
      .splitToList(sortBy);
    var sortingFieldName = sortParts.get(0);
    var sortDirection = SortDirection.fromString(sortParts.get(1));

    Function<InstructorMinimal, String> fieldExtractor = instructor -> {
      if (NAME_FIELD.equalsIgnoreCase(sortingFieldName)) {
        return instructor.getName();
      }
      return instructor.getId();
    };

    Comparator<InstructorMinimal> comparator = Comparator.comparing(fieldExtractor);
    if (sortDirection == SortDirection.DESCENDING) {
      comparator = comparator.reversed();
    }
    return comparator;
  }

}
