package com.ebsco.edgecourses.service;

import static com.ebsco.edgecourses.BaseIntegrationTests.OKAPI_URL;
import static com.ebsco.edgecourses.TestUtil.OBJECT_MAPPER;
import static com.ebsco.edgecourses.TestUtil.RESERVES_RESPONSE_PATH;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    String expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringReserves,
        HttpStatus.OK);
    when(restTemplate
        .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    String reserves = courseReservesService.getCourseReservesByQuery("id=2", EMPTY, EMPTY);

    JsonNode expectedJsonReserves = OBJECT_MAPPER.readTree(expectedStringReserves).get("reserves").get(0);
    JsonNode actualJsonReserves = OBJECT_MAPPER.readTree(reserves).get("reserves").get(0);
    verify(restTemplate)
        .exchange(eq("http://localhost:9130/coursereserves/courses?query=id=2"), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any());
    assertEquals(expectedJsonReserves.get("id"), actualJsonReserves.get("id"));
    assertEquals(expectedJsonReserves.get("courseListingId"), actualJsonReserves.get("courseListingId"));
    assertEquals(expectedJsonReserves.get("53cf956f-c1df-410b-8bea-27f712cca7c0"), actualJsonReserves.get("locationId"));
    assertEquals(expectedJsonReserves.get("12345"), actualJsonReserves.get("courseNumber"));
  }

  @Test
  void getCourseReserveByQuery_shouldReturnReserves_whenQueryEmpty() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringReserves,
        HttpStatus.OK);
    when(restTemplate
        .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    String reserves = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, EMPTY);

    JsonNode expectedJsonReserves = OBJECT_MAPPER.readTree(expectedStringReserves).get("reserves").get(0);
    JsonNode actualJsonReserves = OBJECT_MAPPER.readTree(reserves).get("reserves").get(0);
    verify(restTemplate)
        .exchange(eq("http://localhost:9130/coursereserves/courses"), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any());
    assertEquals(expectedJsonReserves.get("id"), actualJsonReserves.get("id"));
    assertEquals(expectedJsonReserves.get("courseListingId"), actualJsonReserves.get("courseListingId"));
  }

  @Test
  void getCourseReserveById_shouldReturnReserves() throws JsonProcessingException {
    String expectedStringReserves = TestUtil.readFileContentFromResources(RESERVES_RESPONSE_PATH);
    ResponseEntity<String> reservesResponse = new ResponseEntity<>(
        expectedStringReserves,
        HttpStatus.OK);
    when(restTemplate
        .exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any()))
        .thenReturn(reservesResponse);
    String reserves = courseReservesService.getCourseReservesByQuery(EMPTY, EMPTY, EMPTY);

    JsonNode expectedJsonReserves = OBJECT_MAPPER.readTree(expectedStringReserves).get("reserves").get(0);
    JsonNode actualJsonReserves = OBJECT_MAPPER.readTree(reserves).get("reserves").get(0);
    verify(restTemplate)
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), ArgumentMatchers.<Class<String>>any());
    assertEquals(expectedJsonReserves.get("id"), actualJsonReserves.get("id"));
    assertEquals(expectedJsonReserves.get("courseListingId"), actualJsonReserves.get("courseListingId"));
  }

}
