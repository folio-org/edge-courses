package com.ebsco.edgecourses.controller;

import static com.ebsco.edgecourses.TestConstants.COURSES;
import static com.ebsco.edgecourses.TestConstants.RESERVES;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebsco.edgecourses.BaseIntegrationTests;
import com.ebsco.edgecourses.service.CourseReservesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

class CourseReservesControllerIT extends BaseIntegrationTests {

  private static final String COURSE_RESERVE_UUID = "67227d94-7333-4d22-98a0-718b49d36595";
  private static final String COURSE_UUID = "83034b0a-bf71-4495-b642-2e998f721e5d";
  private static final String COURSES_URL_WITH_QUERY = "/courses/courses?query=id=83034b0a-bf71-4495-b642-2e998f721e5d";
  private static final String COURSES_EMPTY_RESPONSE_URL = "/courses/courses?query=id=8f2b60f7-3ff3-4318-be23-95b04b9728c5";
  private static final String RESERVES_URL_BY_ID = "/courses/courselistings/67227d94-7333-4d22-98a0-718b49d36595/reserves";
  private static final String RESERVES_EMPTY_RESPONSE_URL_BY_ID = "/courses/courselistings/8f2b60f7-3ff3-4318-be23-95b04b9728c5/reserves";
  private static final String LANG_PARAM_NAME = "lang";
  private static final String LIMIT_PARAM_NAME = "limit";
  private static final String LANG_PARAM_INVALID_VALUE = "111111";
  private static final String LIMIT_PARAM_INVALID_VALUE = "-1";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CourseReservesService courseReservesService;

  @BeforeEach
  void before() {
    ReflectionTestUtils
        .setField(courseReservesService, "okapiUrl", WIRE_MOCK.baseUrl());
  }

  @ParameterizedTest
  @ValueSource(strings = {COURSES_URL_WITH_QUERY, RESERVES_URL_BY_ID})
  void getCoursesAndReserves_shouldReturnBadRequest_whenLangParamInvalid(String endpoint) throws Exception {
    doGetWithParam(mockMvc, endpoint, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(strings = {COURSES_URL_WITH_QUERY, RESERVES_URL_BY_ID})
  void getCoursesAndReserves_shouldReturnBadRequest_whenLimitInvalid(String endpoint) throws Exception {
    doGetWithParam(mockMvc, endpoint, LIMIT_PARAM_NAME, LIMIT_PARAM_INVALID_VALUE)
        .andExpect(status().isBadRequest());
  }

  @Test
  void getCourses_shouldReturnCourses() throws Exception {
    doGet(mockMvc, COURSES_URL_WITH_QUERY)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(1)))
        .andExpect(jsonPath("courses[0].id", is(COURSE_UUID)));
  }

  @Test
  void getCoursesByInstanceId_shouldReturnCourses() throws Exception {
    doGet(mockMvc, RESERVES_URL_BY_ID)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(1)))
        .andExpect(jsonPath("reserves[0].id", is(COURSE_RESERVE_UUID)));
  }

  @Test
  void getCourses_shouldReturnEmpty_whenCoursesResponseIsEmpty() throws Exception {
    doGet(mockMvc, COURSES_EMPTY_RESPONSE_URL)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)))
        .andExpect(jsonPath(COURSES, iterableWithSize(0)));
  }

  @Test
  void getCoursesByInstanceId_shouldReturnEmpty_whenCoursesByIdResponseIsEmpty() throws Exception {
    doGet(mockMvc, RESERVES_EMPTY_RESPONSE_URL_BY_ID)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)))
        .andExpect(jsonPath(RESERVES, iterableWithSize(0)));
  }

}
