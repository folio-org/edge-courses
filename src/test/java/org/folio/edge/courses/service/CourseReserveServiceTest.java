package org.folio.edge.courses.service;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.folio.edge.courses.TestConstants.ACTIVE_AND_EXPIRED_COURSES_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.COURSES;
import static org.folio.edge.courses.TestConstants.MULTIPLE_COURSES_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.RESERVES;
import static org.folio.edge.courses.TestConstants.RESERVES_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.SHARED_DEPARTMENT_COURSES_RESPONSE_PATH;
import static org.folio.edge.courses.TestConstants.SINGLE_COURSES_RESPONSE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.folio.courses.domain.dto.Courses;
import org.folio.courses.domain.dto.InstructorMinimal;
import org.folio.courses.domain.dto.RequestQueryParameters;
import org.folio.edge.courses.TestUtil;
import org.folio.edge.courses.client.CourseClient;
import org.folio.edge.courses.service.mapper.RequestQueryParametersMapper;
import org.folio.edge.courses.utils.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

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
  @Mock
  private RequestQueryParametersMapper mapper;
  @Mock
  private Map<String, Object> queryParametersMap;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void getCourseReserveByQuery_shouldReturnReserves() {
    //given
    var expectedStringCourses = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = objectMapper.readTree(expectedStringCourses);
    var requestQueryParameters = new RequestQueryParameters().query("id=2");
    when(mapper.toMap(requestQueryParameters)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(reservesResponse);
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
  void getCourseReserveByQuery_shouldReturnReserves_whenQueryEmpty() {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = objectMapper.readTree(expectedStringReserves);
    var requestQueryParameters = new RequestQueryParameters();
    when(mapper.toMap(requestQueryParameters)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(reservesResponse);
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
  void getCourseReserveByQuery_shouldReturnReserves_whileCallingWithAllParams() {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var reservesResponse = objectMapper.readTree(expectedStringReserves);
    var requestQueryParameters = new RequestQueryParameters().query("testQuery");
    when(mapper.toMap(requestQueryParameters)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(reservesResponse);
    //when
    String reserves = courseReservesService.getCoursesByQuery(requestQueryParameters);
    //then
    JsonNode expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(COURSES).get(0);
    JsonNode actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(COURSES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves() {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    var reservesResponse = objectMapper.readTree(expectedStringReserves);
    var requestQueryParameters = new RequestQueryParameters();
    when(mapper.toMap(requestQueryParameters)).thenReturn(queryParametersMap);
    when(courseClient.getReservesByInstanceId(ID, queryParametersMap)).thenReturn(reservesResponse);
    //when
    var reserves = courseReservesService.getReservesByInstanceId(ID, requestQueryParameters);
    //then
    var expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    var actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves_whileCallingWithAllParams() {
    //given
    var expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    var reservesResponse = objectMapper.readTree(expectedStringReserves);
    var requestQueryParameters = setUpQueryParametersWithQueryAndStandardLimit();
    when(mapper.toMap(requestQueryParameters)).thenReturn(queryParametersMap);
    when(courseClient.getReservesByInstanceId("2", queryParametersMap)).thenReturn(reservesResponse);
    //when
    var reserves = courseReservesService.getReservesByInstanceId("2", requestQueryParameters);
    //then
    var expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    var actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getDepartments_shouldReturnDepartments_derivedFromActiveCourses() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(ACTIVE_AND_EXPIRED_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    when(jsonConverter.toJson(any())).thenAnswer(inv -> objectMapper.writeValueAsString(inv.getArgument(0)));
    var queryParameters = new RequestQueryParameters().limit(10).offset(0);
    //when
    var result = courseReservesService.getDepartments(queryParameters);
    //then
    var json = objectMapper.readTree(result);
    assertEquals(1, json.get("totalRecords").asInt());
    assertEquals("1fc91124-cd2a-4fae-9ae4-40368d80982d", json.get("departments").get(0).get("id").asText());
    assertEquals("Mathematics", json.get("departments").get(0).get("name").asText());
  }

  @Test
  void getDepartments_shouldExcludeDepartments_fromExpiredAndNoTermCourses() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(ACTIVE_AND_EXPIRED_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    when(jsonConverter.toJson(any())).thenAnswer(inv -> objectMapper.writeValueAsString(inv.getArgument(0)));
    var queryParameters = new RequestQueryParameters().limit(10).offset(0);
    //when
    var result = courseReservesService.getDepartments(queryParameters);
    //then
    var json = objectMapper.readTree(result);
    var departmentIds = new java.util.ArrayList<String>();
    json.get("departments").forEach(d -> departmentIds.add(d.get("id").asText()));
    assertFalse(departmentIds.contains("332090cd-33af-4f97-aa5f-6a27fd367b63"));
    assertFalse(departmentIds.contains("aabbccdd-ffff-eeee-dddd-ccccbbbbaaaa"));
  }

  @Test
  void getDepartments_shouldDeduplicateDepartments_fromMultipleActiveCoursesWithSameDepartment() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(SHARED_DEPARTMENT_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    when(jsonConverter.toJson(any())).thenAnswer(inv -> objectMapper.writeValueAsString(inv.getArgument(0)));
    var queryParameters = new RequestQueryParameters().limit(10).offset(0);
    //when
    var result = courseReservesService.getDepartments(queryParameters);
    //then
    var json = objectMapper.readTree(result);
    assertEquals(1, json.get("totalRecords").asInt());
    assertEquals("1fc91124-cd2a-4fae-9ae4-40368d80982d", json.get("departments").get(0).get("id").asText());
  }

  @Test
  void getInstructors_shouldExcludeInstructors_fromExpiredCourses() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(ACTIVE_AND_EXPIRED_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    var queryParameters = new RequestQueryParameters().limit(10).offset(0);
    //when
    var instructors = courseReservesService.getInstructors(queryParameters, EMPTY);
    //then
    assertEquals(1, instructors.getTotalRecords());
    assertEquals("2e53ca2f-9bd9-424d-bcef-67f5f268edb0", instructors.getInstructors().get(0).getId());
    assertEquals("Adams Christa A", instructors.getInstructors().get(0).getName());
  }

  @Test
  void getInstructors_shouldExcludeInstructors_fromCoursesWithNullTermObject() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(ACTIVE_AND_EXPIRED_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    var queryParameters = new RequestQueryParameters().limit(10).offset(0);
    //when
    var instructors = courseReservesService.getInstructors(queryParameters, EMPTY);
    //then
    var instructorNames = instructors.getInstructors().stream()
        .map(InstructorMinimal::getName)
        .toList();
    assertFalse(instructorNames.contains("No Term Instructor"));
  }

  @Test
  void getInstructors_shouldReturnInstructors_fromSingleCourseResponse() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    //when
    var instructors = courseReservesService.getInstructors(maxLimit, EMPTY);
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
  void getInstructors_shouldReturnInstructors_andDistinctDuplications() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(MULTIPLE_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    //when
    var instructors = courseReservesService.getInstructors(maxLimit, EMPTY);
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
  void getInstructors_shouldReturnInstructors_sortedByNameInAscendingOrder_whileCallingWithAllParams() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    //when
    var instructors = courseReservesService.getInstructors(maxLimit, "name/sort.ascending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("Aagard Madgeline", instructors.getInstructors().get(0).getName());
    assertEquals("Adams Christa A", instructors.getInstructors().get(1).getName());
    assertEquals("Taylor Mike", instructors.getInstructors().get(2).getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByNameInDescendingOrder_whileCallingWithAllParams() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    //when
    var instructors = courseReservesService.getInstructors(maxLimit, "name/sort.descending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("Taylor Mike", instructors.getInstructors().get(0).getName());
    assertEquals("Adams Christa A", instructors.getInstructors().get(1).getName());
    assertEquals("Aagard Madgeline", instructors.getInstructors().get(2).getName());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByIdInAscendingOrder_whileCallingWithAllParams() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    //when
    var instructors = courseReservesService.getInstructors(maxLimit, "id/sort.ascending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("2e53ca2f-9bd9-424d-bcef-67f5f268edb0", instructors.getInstructors().get(0).getId());
    assertEquals("9cc888e5-f6d7-4709-b113-3040e8fbe648", instructors.getInstructors().get(1).getId());
    assertEquals("f61c6a9e-92b5-470c-8463-6494afd108e6", instructors.getInstructors().get(2).getId());
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByIdInDescendingOrder_whileCallingWithAllParams() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    //when
    var instructors = courseReservesService.getInstructors(maxLimit, "id/sort.descending");
    //then
    assertEquals(3, instructors.getTotalRecords());
    assertEquals("f61c6a9e-92b5-470c-8463-6494afd108e6", instructors.getInstructors().get(0).getId());
    assertEquals("9cc888e5-f6d7-4709-b113-3040e8fbe648", instructors.getInstructors().get(1).getId());
    assertEquals("2e53ca2f-9bd9-424d-bcef-67f5f268edb0", instructors.getInstructors().get(2).getId());
  }

  @Test
  void getInstructors_shouldThrowException_whenSortByValueContainsInvalidDirection() {
    //given
    var coursesJson = TestUtil.readFileContentFromResources(SINGLE_COURSES_RESPONSE_PATH);
    var coursesResponse = objectMapper.readTree(coursesJson);
    var maxLimit = new RequestQueryParameters().limit(Integer.MAX_VALUE);
    Courses courses = objectMapper.readValue(coursesJson, Courses.class);
    when(mapper.toMap(maxLimit)).thenReturn(queryParametersMap);
    when(courseClient.getCourseByQuery(queryParametersMap)).thenReturn(coursesResponse);
    when(jsonConverter.getObjectFromJson(coursesResponse.toString(), Courses.class)).thenReturn(courses);
    //when
    var exception = assertThrows(IllegalArgumentException.class, () ->
        courseReservesService.getInstructors(maxLimit, "id/sort.invalid"));
    //then
    assertEquals("Invalid sort direction: sort.invalid", exception.getMessage());
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
