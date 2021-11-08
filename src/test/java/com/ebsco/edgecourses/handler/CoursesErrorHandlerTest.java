package com.ebsco.edgecourses.handler;

import static com.ebsco.edgecourses.TestConstants.COURSE_BY_ID_FORBIDDEN_URI;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebsco.edgecourses.BaseIntegrationTests;
import com.ebsco.edgecourses.service.CourseReservesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

class CoursesErrorHandlerTest extends BaseIntegrationTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CourseReservesService courseReservesService;

  @BeforeEach
  void before() {
    ReflectionTestUtils.setField(courseReservesService, "okapiUrl", BaseIntegrationTests.WIRE_MOCK.baseUrl());
  }

  @Test
  void handleForbiddenException_shouldProcessException() throws Exception {
    doGet(mockMvc, COURSE_BY_ID_FORBIDDEN_URI)
        .andExpect(status().isForbidden())
        .andExpect(header()
            .stringValues(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("code", is("403")));
  }
}