package org.folio.edge.courses.handler;

import static org.folio.edge.courses.TestConstants.COURSE_BY_ID_FORBIDDEN_URI;
import static org.folio.edge.courses.TestConstants.INSTRUCTORS_URL;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.courses.BaseIntegrationTests;
import org.folio.edge.courses.service.CourseReservesService;
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

  @Test
  void handleForbiddenException_shouldProcessException() throws Exception {
    doGet(mockMvc, COURSE_BY_ID_FORBIDDEN_URI)
      .andExpect(status().isForbidden())
      .andExpect(header()
        .stringValues(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("code", is("403")));
  }

  @Test
  void handleBadRequestException_whenInstructorsRequestHasInvalidSortByParam() throws Exception {
    doGetWithParam(mockMvc, INSTRUCTORS_URL, "sortBy", "invalid")
      .andExpect(status().isBadRequest())
      .andExpect(header()
        .stringValues(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("code", is(400)));
  }
}
