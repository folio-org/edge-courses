package org.folio.edge.courses.service;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import com.google.common.base.Splitter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.courses.domain.dto.Course;
import org.folio.courses.domain.dto.Courselisting;
import org.folio.courses.domain.dto.Courses;
import org.folio.courses.domain.dto.Department;
import org.folio.courses.domain.dto.Instructor;
import org.folio.courses.domain.dto.InstructorMinimal;
import org.folio.courses.domain.dto.Instructors;
import org.folio.courses.domain.dto.RequestQueryParameters;
import org.folio.edge.courses.client.CourseClient;
import org.folio.edge.courses.model.dto.DepartmentsResponse;
import org.folio.edge.courses.model.dto.SortDirection;
import org.folio.edge.courses.utils.JsonConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseReservesService {

  private static final String NAME_FIELD = "name";
  private static final String SLASH_SEPARATOR = "/";

  private final CourseClient courseClient;
  private final JsonConverter jsonConverter;

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
    log.info("Retrieving departments for active courses");
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    var coursesResponse = courseClient.getCourseByQuery(maxLimit);
    var courses = jsonConverter.getObjectFromJson(coursesResponse.getBody(), Courses.class);
    log.info("Received courses response while getting departments, size: {}", courses::getTotalRecords);

    Set<String> seenDepartmentIds = new HashSet<>();
    var allDepartments = courses.getCourses().stream()
      .filter(this::isActiveCourse)
      .map(Course::getDepartmentObject)
      .filter(Objects::nonNull)
      .filter(d -> seenDepartmentIds.add(d.getId()))
      .toList();

    var paged = paginateDepartments(allDepartments, queryParameters.getOffset(), queryParameters.getLimit());
    return jsonConverter.toJson(new DepartmentsResponse(paged, allDepartments.size()));
  }

  public Instructors getInstructors(RequestQueryParameters queryParameters, String sortBy) {
    log.info("Calling getInstructors");
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    var coursesResponse = courseClient.getCourseByQuery(maxLimit);
    var courses = jsonConverter.getObjectFromJson(coursesResponse.getBody(), Courses.class);
    log.info("Received courses response while getting instructors, size: {}", courses::getTotalRecords);
    Set<String> seenInstructorIds = new HashSet<>();
    var courseInstructors = courses.getCourses().stream()
        .filter(this::isActiveCourse)
        .map(Course::getCourseListingObject)
        .filter(Objects::nonNull)
        .map(Courselisting::getInstructorObjects)
        .flatMap(Collection::stream)
        .map(this::toInstructorMinimal)
        .filter(i -> seenInstructorIds.add(i.getId()))
        .toList();

    if (isNoneBlank(sortBy)) {
      courseInstructors = courseInstructors.stream()
        .sorted(getInstructorMinimalComparator(sortBy))
        .toList();
    }

    var instructors = paginateInstructors(courseInstructors, queryParameters.getOffset(), queryParameters.getLimit());
    return new Instructors(instructors, courseInstructors.size());
  }

  private boolean isActiveCourse(Course course) {
    try {
      var courseListingObject = course.getCourseListingObject();
      var term = courseListingObject.getTermObject();
      return isDateActive(term.getStartDate(), term.getEndDate());
    } catch (Exception e) {
      log.warn("Cannot validate course dates to check status");
      return false;
    }
  }

  private boolean isDateActive(String startDate, String endDate) {
    var startDateInstant = getInstantFromString(startDate);
    var endDateInstant = getInstantFromString(endDate);
    return isCurrentDateBetween(startDateInstant, endDateInstant);
  }

  public boolean isCurrentDateBetween(Instant startDate, Instant endDate) {
    var currentDate = Instant.now();
    return !(currentDate.isBefore(startDate) || currentDate.isAfter(endDate));
  }

  private Instant getInstantFromString(String date) {
    try {
      return Instant.parse(date);
    } catch (DateTimeParseException e) {
      log.warn("Cannot parse date {} to Instant, trying to parse Instant from short date format", date);
      return getInstantFromShortDate(date);
    }
  }

  private Instant getInstantFromShortDate(String date) {
    var localDate = LocalDate.parse(date);
    var localDateTime = localDate.atStartOfDay();
    return localDateTime.toInstant(ZoneOffset.UTC);
  }

  private List<Department> paginateDepartments(List<Department> departments, Integer offset, Integer limit) {
    int size = departments.size();
    if (size <= offset) {
      return Collections.emptyList();
    }
    limit = Math.min(limit + offset, size);
    return departments.subList(offset, limit);
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
