package com.ebsco.edge.courses.service;

import static com.ebsco.edge.courses.TestConstants.COURSELISTINGS_RESPONSE_PATH;
import static com.ebsco.edge.courses.TestConstants.COURSES;
import static com.ebsco.edge.courses.TestConstants.COURSES_RESPONSE_PATH;
import static com.ebsco.edge.courses.TestConstants.DEPARTMENTS_RESPONSE_PATH;
import static com.ebsco.edge.courses.TestConstants.RESERVES;
import static com.ebsco.edge.courses.TestConstants.RESERVES_RESPONSE_PATH;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.ebsco.courses.domain.dto.Courselistings;
import com.ebsco.courses.domain.dto.Instructors;
import com.ebsco.courses.domain.dto.LocateInstructor;
import com.ebsco.courses.domain.dto.RequestQueryParameters;
import com.ebsco.edge.courses.TestUtil;
import com.ebsco.edge.courses.client.CourseClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CourseReserveServiceTest {

  private static final String OKAPI_URL = "http://localhost:9130";
  private static final String COURSE_LISTING_ID = "courseListingId";
  private static final String ID = "id";
  private static final String COURSE_NUMBER = "courseNumber";
  private static final String LOCATION_ID = "locationId";

  @InjectMocks
  private CourseReservesService courseReservesService;
  @Mock
  private CourseClient courseClient;

  @BeforeEach
  void before() {
    ReflectionTestUtils
        .setField(courseReservesService, "okapiUrl", OKAPI_URL);
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves() throws JsonProcessingException {
    String expectedStringCourses = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringCourses,
        HttpStatus.OK);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters().query("id=2");
    when(courseClient.getCourseByQuery(URI.create(OKAPI_URL), EMPTY, EMPTY, requestQueryParameters))
        .thenReturn(reservesResponse);
    String courses = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonCourses = TestUtil.OBJECT_MAPPER.readTree(expectedStringCourses).get(COURSES).get(0);
    JsonNode actualJsonCourses = TestUtil.OBJECT_MAPPER.readTree(courses).get(COURSES).get(0);
    assertEquals(expectedJsonCourses.get(ID), actualJsonCourses.get(ID));
    assertEquals(expectedJsonCourses.get(COURSE_LISTING_ID), actualJsonCourses.get(COURSE_LISTING_ID));
    assertEquals(expectedJsonCourses.get(COURSE_NUMBER), actualJsonCourses.get(COURSE_NUMBER));
    assertEquals(expectedJsonCourses.get(LOCATION_ID), actualJsonCourses.get(LOCATION_ID));
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves_whenQueryEmpty() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(courseClient.getCourseByQuery(URI.create(OKAPI_URL), EMPTY, EMPTY, requestQueryParameters))
        .thenReturn(reservesResponse);

    String reserves = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(COURSES).get(0);
    JsonNode actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(COURSES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
    assertEquals(expectedJsonReserves.get(COURSE_NUMBER), actualJsonReserves.get(COURSE_NUMBER));
    assertEquals(expectedJsonReserves.get(LOCATION_ID), actualJsonReserves.get(LOCATION_ID));
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves_whileCallingWithAllParams() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters().query("testQuery");
    when(courseClient.getCourseByQuery(URI.create(OKAPI_URL), EMPTY, EMPTY, requestQueryParameters))
        .thenReturn(reservesResponse);
    
    String reserves = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(COURSES).get(0);
    JsonNode actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(COURSES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    when(courseClient.getCourseById(URI.create(OKAPI_URL), ID, EMPTY, EMPTY, requestQueryParameters))
        .thenReturn(reservesResponse);
    
    String reserves = courseReservesService
        .getCourseReservesByInstanceId(ID, EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    JsonNode actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves_whileCallingWithAllParams() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    var reservesResponse = ResponseEntity.ok(expectedStringReserves);
    
    RequestQueryParameters requestQueryParameters = createRequestQueryParameters();
    when(courseClient.getCourseById(URI.create(OKAPI_URL), "2", EMPTY, EMPTY, requestQueryParameters))
        .thenReturn(reservesResponse);
    
    String reserves = courseReservesService
        .getCourseReservesByInstanceId("2", EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = TestUtil.OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    JsonNode actualJsonReserves = TestUtil.OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getDepartments_shouldReturnDepartments_whileCallingWithAllParams() throws JsonProcessingException {
    String departmentsJson = TestUtil.readFileContentFromResources(DEPARTMENTS_RESPONSE_PATH);
    var departmentsRespEntity = ResponseEntity.ok(departmentsJson);
    
    RequestQueryParameters requestQueryParameters = createRequestQueryParameters();
    
    when(courseClient.getDepartments(URI.create(OKAPI_URL), EMPTY, EMPTY, requestQueryParameters))
        .thenReturn(departmentsRespEntity);
    
    String departments = courseReservesService.getDepartments(EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonDepartments = TestUtil.OBJECT_MAPPER.readTree(departmentsJson).get("departments").get(0);
    JsonNode actualJsonDepartments = TestUtil.OBJECT_MAPPER.readTree(departments).get("departments").get(0);

    assertThat(actualJsonDepartments, is(samePropertyValuesAs(expectedJsonDepartments)));
    assertEquals(expectedJsonDepartments.get(ID), actualJsonDepartments.get(ID));
    assertEquals(expectedJsonDepartments.get("name"), actualJsonDepartments.get("name"));
  }

  @Test
  void getInstructors_shouldReturnInstructors_whileCallingWithAllParams() throws JsonProcessingException {
    String courseListingsJson = TestUtil.readFileContentFromResources(COURSELISTINGS_RESPONSE_PATH);
    Courselistings courselistings = 
        TestUtil.OBJECT_MAPPER.readValue(courseListingsJson, Courselistings.class);

    RequestQueryParameters requestQueryParameters = new RequestQueryParameters()
        .limit(Integer.MAX_VALUE)
        .offset(0)
        .lang("en");
    
    when(courseClient.getCourselistings(URI.create(OKAPI_URL), EMPTY, EMPTY, requestQueryParameters))
        .thenReturn(courselistings);

    Instructors instructors = courseReservesService.getInstructors(EMPTY, EMPTY, requestQueryParameters);

    LocateInstructor actualInstructor = instructors.getInstructors().get(0);
    assertEquals("75985fb0-a994-4136-a2bf-ec5824f43877", actualInstructor.getId());
    assertEquals("Emma Beck", actualInstructor.getName());
  }

  private static RequestQueryParameters createRequestQueryParameters() {
    return new RequestQueryParameters()
        .query("testQuery")
        .expand("true")
        .lang("en")
        .limit(10)
        .offset(0);
  }

}
