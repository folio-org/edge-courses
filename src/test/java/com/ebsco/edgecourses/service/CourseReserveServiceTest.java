package com.ebsco.edgecourses.service;

import static com.ebsco.edgecourses.BaseIntegrationTests.OKAPI_URL;
import static com.ebsco.edgecourses.TestUtil.COURSES;
import static com.ebsco.edgecourses.TestUtil.COURSES_RESPONSE_PATH;
import static com.ebsco.edgecourses.TestUtil.OBJECT_MAPPER;
import static com.ebsco.edgecourses.TestUtil.RESERVES;
import static com.ebsco.edgecourses.TestUtil.RESERVES_RESPONSE_PATH;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.ebsco.courses.domain.dto.RequestQueryParameters;
import com.ebsco.edgecourses.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class CourseReserveServiceTest {

  private static final String COURSE_LISTING_ID = "courseListingId";
  private static final String COURSES_URL_PART = "/coursereserves/courses";
  private static final String RESERVES_URL_PART = "/coursereserves/courselistings/%s/reserves";
  private static final String ID = "id";
  private static final String COURSE_NUMBER = "courseNumber";
  private static final String LOCATION_ID = "locationId";

  @InjectMocks
  private CourseReservesService courseReservesService;
  @Mock
  private RestTemplate restTemplate;

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
    when(restTemplate
        .exchange(eq(OKAPI_URL + COURSES_URL_PART + "?query=id=2&limit=10&offset=0&lang=en"), eq(HttpMethod.GET),
            any(HttpEntity.class), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters().query("id=2");
    String courses = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonCourses = OBJECT_MAPPER.readTree(expectedStringCourses).get(COURSES).get(0);
    JsonNode actualJsonCourses = OBJECT_MAPPER.readTree(courses).get(COURSES).get(0);
    assertEquals(expectedJsonCourses.get(ID), actualJsonCourses.get(ID));
    assertEquals(expectedJsonCourses.get(COURSE_LISTING_ID), actualJsonCourses.get(COURSE_LISTING_ID));
    assertEquals(expectedJsonCourses.get(COURSE_NUMBER), actualJsonCourses.get(COURSE_NUMBER));
    assertEquals(expectedJsonCourses.get(LOCATION_ID), actualJsonCourses.get(LOCATION_ID));
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves_whenQueryEmpty() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringReserves,
        HttpStatus.OK);
    when(restTemplate
        .exchange(eq(OKAPI_URL + COURSES_URL_PART + "?limit=10&offset=0&lang=en"), eq(HttpMethod.GET),
            any(HttpEntity.class),
            ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    String reserves = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = OBJECT_MAPPER.readTree(expectedStringReserves).get(COURSES).get(0);
    JsonNode actualJsonReserves = OBJECT_MAPPER.readTree(reserves).get(COURSES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
    assertEquals(expectedJsonReserves.get(COURSE_NUMBER), actualJsonReserves.get(COURSE_NUMBER));
    assertEquals(expectedJsonReserves.get(LOCATION_ID), actualJsonReserves.get(LOCATION_ID));
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves_whileCallingWithAllParams() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(COURSES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringReserves,
        HttpStatus.OK);
    when(restTemplate
        .exchange(eq(OKAPI_URL + COURSES_URL_PART + "?query=testQuery&limit=10&offset=0&lang=en"),
            eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters().query("testQuery");
    String reserves = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = OBJECT_MAPPER.readTree(expectedStringReserves).get(COURSES).get(0);
    JsonNode actualJsonReserves = OBJECT_MAPPER.readTree(reserves).get(COURSES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringReserves,
        HttpStatus.OK);
    when(restTemplate
        .exchange(eq(OKAPI_URL + String.format(RESERVES_URL_PART, ID) + "?limit=10&offset=0&lang=en"),
            eq(HttpMethod.GET),
            any(HttpEntity.class), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters();
    String reserves = courseReservesService
        .getCourseReservesByInstanceId(ID, EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    JsonNode actualJsonReserves = OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves_whileCallingWithAllParams() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringReserves,
        HttpStatus.OK);
    when(restTemplate
        .exchange(
            eq(OKAPI_URL + String.format(RESERVES_URL_PART, 2)
                + "?query=testQuery&limit=10&offset=0&lang=en&expand=true"),
            eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    RequestQueryParameters requestQueryParameters = new RequestQueryParameters().query("testQuery").expand("true")
        .lang("en")
        .limit(10).offset(0);
    String reserves = courseReservesService
        .getCourseReservesByInstanceId("2", EMPTY, EMPTY, requestQueryParameters);

    JsonNode expectedJsonReserves = OBJECT_MAPPER.readTree(expectedStringReserves).get(RESERVES).get(0);
    JsonNode actualJsonReserves = OBJECT_MAPPER.readTree(reserves).get(RESERVES).get(0);
    assertEquals(expectedJsonReserves.get(ID), actualJsonReserves.get(ID));
    assertEquals(expectedJsonReserves.get(COURSE_LISTING_ID), actualJsonReserves.get(COURSE_LISTING_ID));
  }

}
