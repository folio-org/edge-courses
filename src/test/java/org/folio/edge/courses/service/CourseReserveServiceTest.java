package org.folio.edge.courses.service;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.folio.edge.courses.TestConstants.COURSES;
import static org.folio.edge.courses.TestConstants.MULTIPLE_COURSES_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.SINGLE_COURSES_RESPONSE_PATH;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.folio.courses.domain.dto.Courses;
import org.folio.courses.domain.dto.RequestQueryParameters;
import org.folio.edge.courses.TestUtil;
import org.folio.edge.courses.client.CourseClient;
import org.folio.edge.courses.utils.JsonConverter;
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
  @Mock
  private JsonConverter jsonConverter;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void getCourseReserveByQuery_shouldReturnReserves() throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
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
    var expectedStringReserves = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
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
    var expectedStringReserves = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
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
  void getInstructors_shouldReturnInstructors_fromSingleCourseResponse() throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    ObjectMapper objectMapper = new ObjectMapper();
    Courses expectedCourses = objectMapper.readValue(expectedStringCourses, Courses.class);
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    when(jsonConverter.getObjectFromJson(expectedStringCourses, Courses.class)).thenReturn(expectedCourses);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters, EMPTY);
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("2e53ca2f-9bd9-424d-bcef-67f5f268edb0", instructors.getInstructors().get(0).getId());
    assertEquals("Adams Christa A", instructors.getInstructors().get(0).getName());
    assertEquals("f61c6a9e-92b5-470c-8463-6494afd108e6", instructors.getInstructors().get(1).getId());
    assertEquals("Taylor Mike", instructors.getInstructors().get(1).getName());
    assertEquals("9cc888e5-f6d7-4709-b113-3040e8fbe648", instructors.getInstructors().get(2).getId());
    assertEquals("Aagard Madgeline", instructors.getInstructors().get(2).getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_andDistinctDuplications() throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(MULTIPLE_COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses expectedCourses = objectMapper.readValue(expectedStringCourses, Courses.class);
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    when(jsonConverter.getObjectFromJson(expectedStringCourses, Courses.class)).thenReturn(expectedCourses);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters, EMPTY);
    //then
    assertEquals(4, instructors.getTotalRecords());
    assertEquals("2e53ca2f-9bd9-424d-bcef-67f5f268edb0", instructors.getInstructors().get(0).getId());
    assertEquals("Adams Christa A", instructors.getInstructors().get(0).getName());
    assertEquals("f61c6a9e-92b5-470c-8463-6494afd108e6", instructors.getInstructors().get(1).getId());
    assertEquals("Taylor Mike", instructors.getInstructors().get(1).getName());
    assertEquals("9cc888e5-f6d7-4709-b113-3040e8fbe648", instructors.getInstructors().get(2).getId());
    assertEquals("Aagard Madgeline", instructors.getInstructors().get(2).getName());
    assertEquals("10401bcf-d178-4b0e-8dda-c426c727c30d", instructors.getInstructors().get(3).getId());
    assertEquals("Kim Ammons", instructors.getInstructors().get(3).getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByNameInAscendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses expectedCourses = objectMapper.readValue(expectedStringCourses, Courses.class);
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    when(jsonConverter.getObjectFromJson(expectedStringCourses, Courses.class)).thenReturn(expectedCourses);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "name/sort.ascending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("Aagard Madgeline", instructors.getInstructors().get(0).getName());
    assertEquals("Adams Christa A", instructors.getInstructors().get(1).getName());
    assertEquals("Taylor Mike", instructors.getInstructors().get(2).getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByNameInDescendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses expectedCourses = objectMapper.readValue(expectedStringCourses, Courses.class);
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    when(jsonConverter.getObjectFromJson(expectedStringCourses, Courses.class)).thenReturn(expectedCourses);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "name/sort.descending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("Taylor Mike", instructors.getInstructors().get(0).getName());
    assertEquals("Adams Christa A", instructors.getInstructors().get(1).getName());
    assertEquals("Aagard Madgeline", instructors.getInstructors().get(2).getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByIdInAscendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses expectedCourses = objectMapper.readValue(expectedStringCourses, Courses.class);
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    when(jsonConverter.getObjectFromJson(expectedStringCourses, Courses.class)).thenReturn(expectedCourses);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "id/sort.ascending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("2e53ca2f-9bd9-424d-bcef-67f5f268edb0", instructors.getInstructors().get(0).getId());
    assertEquals("9cc888e5-f6d7-4709-b113-3040e8fbe648", instructors.getInstructors().get(1).getId());
    assertEquals("f61c6a9e-92b5-470c-8463-6494afd108e6", instructors.getInstructors().get(2).getId());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByIdInDescendingOrder_whileCallingWithAllParams()
    throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses expectedCourses = objectMapper.readValue(expectedStringCourses, Courses.class);
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    when(jsonConverter.getObjectFromJson(expectedStringCourses, Courses.class)).thenReturn(expectedCourses);
    //when
    var instructors = courseReservesService.getInstructors(requestQueryParameters,
      "id/sort.descending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("f61c6a9e-92b5-470c-8463-6494afd108e6", instructors.getInstructors().get(0).getId());
    assertEquals("9cc888e5-f6d7-4709-b113-3040e8fbe648", instructors.getInstructors().get(1).getId());
    assertEquals("2e53ca2f-9bd9-424d-bcef-67f5f268edb0", instructors.getInstructors().get(2).getId());
  }

  @Test
  void getInstructors_shouldThrowException_whenSortByValueContainsInvalidDirection()
    throws JsonProcessingException {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = new ResponseEntity<>(expectedStringCourses, HttpStatus.OK);
    var requestQueryParameters = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses expectedCourses = objectMapper.readValue(expectedStringCourses, Courses.class);
    when(courseClient.getCourseByQuery(requestQueryParameters)).thenReturn(reservesResponse);
    when(jsonConverter.getObjectFromJson(expectedStringCourses, Courses.class)).thenReturn(expectedCourses);
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
