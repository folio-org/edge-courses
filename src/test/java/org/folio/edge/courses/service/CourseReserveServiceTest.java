package org.folio.edge.courses.service;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.folio.edge.courses.TestConstants.COURSELISTINGS_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.COURSES;
import static org.folio.edge.courses.TestConstants.COURSES_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.DEPARTMENTS_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.RESERVES;
import static org.folio.edge.courses.TestConstants.RESERVES_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.RESERVES_WITHOUT_QUERY_RESPONSE_PATH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.folio.courses.domain.dto.Courselistings;
import org.folio.courses.domain.dto.RequestQueryParameters;
import org.folio.edge.courses.TestUtil;
import org.folio.edge.courses.client.CourseClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CourseReserveServiceTest {

  private static final String COURSE_LISTING_ID = "courseListingId";
  private static final String ID = "id";
  private static final String COURSE_NUMBER = "courseNumber";
  private static final String LOCATION_ID = "locationId";

  @InjectMocks
  private CourseReservesService courseReservesService;
  @Mock
  private CourseClient courseClient;

  @Test
  void getCourseReserveByQuery_shouldReturnReserves() throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().query("id=2");
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    //when
    var courses = courseReservesService.getCoursesByQuery(requestQueryParameters);
    //then
    var expectedJsonCourses = TestUtil.OBJECT_MAPPER.readTree(expectedStringCourses).get(COURSES).get(0);
    var actualJsonCourses = TestUtil.OBJECT_MAPPER.readTree(courses).get(COURSES).get(0);
    assertEquals(expectedJsonCourses.get(ID), actualJsonCourses.get(ID));
    assertEquals(expectedJsonCourses.get(COURSE_LISTING_ID), actualJsonCourses.get(COURSE_LISTING_ID));
    assertEquals(expectedJsonCourses.get(COURSE_NUMBER), actualJsonCourses.get(COURSE_NUMBER));
    assertEquals(expectedJsonCourses.get(LOCATION_ID), actualJsonCourses.get(LOCATION_ID));
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves_whenQueryEmpty() throws JsonProcessingException {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    var requestQueryParameters = new RequestQueryParameters();
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    //when
    var reserves = courseReservesService.getCoursesByQuery(requestQueryParameters);
    //then
    var expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(COURSES).get(0);
    var actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(COURSES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
    assertEquals(expectedJsonReserves.get(COURSE_NUMBER), actualJsonReserves.get(COURSE_NUMBER));
    assertEquals(expectedJsonReserves.get(LOCATION_ID), actualJsonReserves.get(LOCATION_ID));
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves_whileCallingWithAllParams() throws JsonProcessingException {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    var requestQueryParameters = new RequestQueryParameters().query("testQuery");
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    //when
    String reserves = courseReservesService.getCoursesByQuery(requestQueryParameters);
    //then
    JsonNode expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(COURSES).get(0);
    JsonNode actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(COURSES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves() throws JsonProcessingException {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    var requestQueryParameters = new RequestQueryParameters();
    when(courseClient.getReservesByInstanceId(ID, requestQueryParameters)).thenReturn(reservesResponse);
    //when
    var reserves = courseReservesService.getReservesByInstanceId(ID, requestQueryParameters);
    //then
    var expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    var actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves_whileCallingWithAllParams() throws JsonProcessingException {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    var requestQueryParameters = setUpQueryParametersWithQueryAndStandardLimit();
    when(courseClient.getReservesByInstanceId("2", requestQueryParameters)).thenReturn(reservesResponse);
    //when
    var reserves = courseReservesService.getReservesByInstanceId("2", requestQueryParameters);
    //then
    var expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    var actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getDepartments_shouldReturnDepartments_whileCallingWithAllParams() throws JsonProcessingException {
    //given
    var departmentsJson = TestUtil.readFileContentFromResources(DEPARTMENTS_RESPONSE_PATH);
    var departmentsRespEntity = ResponseEntity.ok(departmentsJson);
    var requestQueryParameters = setUpQueryParametersWithQueryAndStandardLimit();
    when(courseClient.getDepartments(requestQueryParameters)).thenReturn(departmentsRespEntity);
    //when
    var departments = courseReservesService.getDepartments(requestQueryParameters);
    //then
    var expectedJsonDepartments = TestUtil.OBJECT_MAPPER.readTree(departmentsJson).get("departments").get(0);
    var actualJsonDepartments = TestUtil.OBJECT_MAPPER.readTree(departments).get("departments").get(0);
    assertThat(actualJsonDepartments, is(samePropertyValuesAs(expectedJsonDepartments)));
    assertEquals(expectedJsonDepartments.get(ID), actualJsonDepartments.get(ID));
    assertEquals(expectedJsonDepartments.get("name"), actualJsonDepartments.get("name"));
  }

  @Test
  void getInstructors_shouldReturnInstructors_whileCallingWithAllParams() throws JsonProcessingException {
    //given
    var courseListingsJson = TestUtil.readFileContentFromResources(COURSELISTINGS_RESPONSE_PATH);
    var courselistings = TestUtil.OBJECT_MAPPER.readValue(courseListingsJson, Courselistings.class);
    var requestQueryParameters = setUpQueryParametersWithMaxLimit();
    when(courseClient.getCourselistings(requestQueryParameters)).thenReturn(courselistings);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters, EMPTY);
    //then
    var actualInstructor = instructors.getInstructors().get(0);
    assertEquals(11, instructors.getTotalRecords());
    assertEquals("75985fb0-a994-4136-a2bf-ec5824f43877", actualInstructor.getId());
    assertEquals("Emma Beck", actualInstructor.getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByNameInAscendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var courseListingsJson = TestUtil.readFileContentFromResources(COURSELISTINGS_RESPONSE_PATH);
    var courselistings = TestUtil.OBJECT_MAPPER.readValue(courseListingsJson, Courselistings.class);
    var requestQueryParameters = setUpQueryParametersWithMaxLimit();
    when(courseClient.getCourselistings(requestQueryParameters)).thenReturn(courselistings);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "name/sort.ascending");
    //then
    var actualInstructor = instructors.getInstructors().get(0);
    assertEquals(11, instructors.getTotalRecords());
    assertEquals("459456a2-6415-44b3-8b46-2f3bbe563bbb", actualInstructor.getId());
    assertEquals("Alastor \"Mad-Eye\" Moody", actualInstructor.getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByNameInDescendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var courseListingsJson = TestUtil.readFileContentFromResources(COURSELISTINGS_RESPONSE_PATH);
    var courselistings = TestUtil.OBJECT_MAPPER.readValue(courseListingsJson, Courselistings.class);
    var requestQueryParameters = setUpQueryParametersWithMaxLimit();
    when(courseClient.getCourselistings(requestQueryParameters)).thenReturn(courselistings);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "name/sort.descending");
    //then
    var actualInstructor = instructors.getInstructors().get(0);
    assertEquals(11, instructors.getTotalRecords());
    assertEquals("20b8903a-0892-4af8-97c1-7e46c514c1c5", actualInstructor.getId());
    assertEquals("Serverus Snape", actualInstructor.getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByIdInAscendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var courseListingsJson = TestUtil.readFileContentFromResources(COURSELISTINGS_RESPONSE_PATH);
    var courselistings = TestUtil.OBJECT_MAPPER.readValue(courseListingsJson, Courselistings.class);
    var requestQueryParameters = setUpQueryParametersWithMaxLimit();
    when(courseClient.getCourselistings(requestQueryParameters)).thenReturn(courselistings);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "id/sort.ascending");
    //then
    var actualInstructor = instructors.getInstructors().get(0);
    assertEquals(11, instructors.getTotalRecords());
    assertEquals("02d9b8ca-bda2-4622-a4df-83ca19f63e86", actualInstructor.getId());
    assertEquals("Albus Dumbledore", actualInstructor.getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByIdInDescendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var courseListingsJson = TestUtil.readFileContentFromResources(COURSELISTINGS_RESPONSE_PATH);
    var courselistings = TestUtil.OBJECT_MAPPER.readValue(courseListingsJson, Courselistings.class);
    var requestQueryParameters = setUpQueryParametersWithMaxLimit();
    when(courseClient.getCourselistings(requestQueryParameters)).thenReturn(courselistings);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "id/sort.descending");
    //then
    var actualInstructor = instructors.getInstructors().get(0);
    assertEquals(11, instructors.getTotalRecords());
    assertEquals("f711647c-7e57-47c3-a957-d996a0adbdf8", actualInstructor.getId());
    assertEquals("Gabe Stetson", actualInstructor.getName());
  }

  @Test
  void getInstructors_shouldThrowException_whenSortByValueContainsInvalidDirection()
    throws JsonProcessingException {
    //given
    var courseListingsJson = TestUtil.readFileContentFromResources(COURSELISTINGS_RESPONSE_PATH);
    var courselistings = TestUtil.OBJECT_MAPPER.readValue(courseListingsJson, Courselistings.class);
    var requestQueryParameters = setUpQueryParametersWithMaxLimit();
    when(courseClient.getCourselistings(requestQueryParameters)).thenReturn(courselistings);
    //when
    var exception = assertThrows(IllegalArgumentException.class, () -> {
      courseReservesService.getInstructors(requestQueryParameters, "id/sort.invalid");
    });
    //then
    assertEquals("Invalid sort direction: sort.invalid", exception.getMessage());
  }

  @Test
  void getReserves_shouldReturnReserves() {
    //given
    var expectedReserves = TestUtil.readFileContentFromResources(RESERVES_WITHOUT_QUERY_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedReserves, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters();
    when(courseClient.getReservesByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    //when
    var actualReserves = courseReservesService.getReservesByQuery(requestQueryParameters);
    //then
    assertEquals(expectedReserves, actualReserves);
  }

  private RequestQueryParameters setUpQueryParametersWithMaxLimit() {
    return new RequestQueryParameters()
      .limit(Integer.MAX_VALUE)
      .offset(0)
      .lang("en");
  }

  private RequestQueryParameters setUpQueryParametersWithQueryAndStandardLimit() {
    return new RequestQueryParameters()
      .query("testQuery")
      .expand("true")
      .lang("en")
      .limit(10)
      .offset(0);
  }

}
