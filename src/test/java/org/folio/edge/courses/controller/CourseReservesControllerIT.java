package org.folio.edge.courses.controller;

import static org.folio.edge.courses.TestConstants.COURSES;
import static org.folio.edge.courses.TestConstants.INSTRUCTORS_URL;
import static org.folio.edge.courses.TestConstants.RESERVES;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import org.folio.edge.courses.BaseIntegrationTests;
import org.folio.edge.courses.service.CourseReservesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

class CourseReservesControllerIT extends BaseIntegrationTests {

  private static final String COURSE_RESERVE_UUID = "67227d94-7333-4d22-98a0-718b49d36595";
  private static final String COURSE_UUID = "83034b0a-bf71-4495-b642-2e998f721e5d";
  private static final String DEPARTMENTS_URL = "/courses/departments";
  private static final String DEPARTMENTS_URL_WITH_PARAM = "/courses/departments?offset=0&limit=10&lang=en";
  private static final String COURSES_URL_WITH_QUERY = "/courses/courses?query=id=83034b0a-bf71-4495-b642-2e998f721e5d";
  private static final String RESERVES_URL = "/courses/reserves";
  private static final String RESERVES_URL_WITH_QUERY = "/courses/reserves?query=courseListingId=a9fa4530-7c38-42b1-a6a9-bd5964039b19";
  private static final String RESERVES_EMPTY_RESPONSE_URL = "/courses/reserves?query=courseListingId=a9fa4530-7c38-42b1-a6a9-bd5964031111";
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

  @ParameterizedTest
  @ValueSource(strings = {COURSES_URL_WITH_QUERY, RESERVES_URL_BY_ID, DEPARTMENTS_URL, INSTRUCTORS_URL})
  void getCoursesAndReserves_shouldReturnBadRequest_whenLangParamInvalid(String endpoint)
    throws Exception {
    doGetWithParam(mockMvc, endpoint, LANG_PARAM_NAME, LANG_PARAM_INVALID_VALUE)
      .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @ValueSource(strings = {COURSES_URL_WITH_QUERY, RESERVES_URL_BY_ID, INSTRUCTORS_URL, DEPARTMENTS_URL})
  void getCoursesAndReserves_shouldReturnBadRequest_whenLimitInvalid(String endpoint)
    throws Exception {
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
  void getDepartments_shouldReturnDepartments() throws Exception {
    doGet(mockMvc, DEPARTMENTS_URL_WITH_PARAM)
      .andExpect(status().isOk())
      .andExpect(jsonPath("totalRecords", is(145)))
      .andExpect(jsonPath("departments", hasSize(10)));
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

  @ParameterizedTest(name = "Offset {0} and limit {1}")
  @ArgumentsSource(LimitAndOffsetProvider.class)
  void getInstructors_shouldReturnInstructors(Integer offset, Integer limit, Integer resultSize,
    String[] items) throws Exception {
    doGetWithLimitAndOffset(mockMvc, INSTRUCTORS_URL, limit, offset)
      .andExpect(status().isOk())
      .andExpect(jsonPath("totalRecords", is(4)))
      .andExpect(jsonPath("instructors", hasSize(resultSize)))
      .andExpect(jsonPath("instructors[*].name", hasItems(items)));
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedByName() throws Exception {
    doGetWithParam(mockMvc, INSTRUCTORS_URL, "sortBy", "name/sort.ascending")
      .andExpect(status().isOk())
      .andExpect(jsonPath("totalRecords", is(4)))
      .andExpect(jsonPath("instructors[0].name", is("Aagard Madgeline")))
      .andExpect(jsonPath("instructors[0].id", is("9cc888e5-f6d7-4709-b113-3040e8fbe648")));
  }

  @Test
  void getInstructors_shouldReturnInstructors_sortedById() throws Exception {
    doGetWithParam(mockMvc, INSTRUCTORS_URL, "sortBy", "id/sort.descending")
      .andExpect(status().isOk())
      .andExpect(jsonPath("totalRecords", is(4)))
      .andExpect(jsonPath("instructors[0].name", is("Taylor Mike")))
      .andExpect(jsonPath("instructors[0].id", is("f61c6a9e-92b5-470c-8463-6494afd108e6")));
  }

  @Test
  void getReserves_shouldReserves() throws Exception {
    doGet(mockMvc, RESERVES_URL)
      .andExpect(status().isOk())
      .andExpect(jsonPath("totalRecords", is(4)))
      .andExpect(jsonPath("reserves[0].id", is("01be009e-46c0-4c46-9e01-d9604629a005")))
      .andExpect(jsonPath("reserves[1].id", is("e79e2506-30d9-4e4b-89d2-c2502140b889")))
      .andExpect(jsonPath("reserves[2].id", is("91441bd6-6f0f-4586-a4a5-26ffcfb20e0c")))
      .andExpect(jsonPath("reserves[3].id", is("76437744-b496-4c49-9466-12d14b293c4a")));
  }

  @Test
  void getReserves_shouldReservesByQuery() throws Exception {
    doGet(mockMvc, RESERVES_URL_WITH_QUERY)
      .andExpect(status().isOk())
      .andExpect(jsonPath("totalRecords", is(2)))
      .andExpect(jsonPath("reserves[0].id", is("01be009e-46c0-4c46-9e01-d9604629a005")))
      .andExpect(jsonPath("reserves[1].id", is("e79e2506-30d9-4e4b-89d2-c2502140b889")));
  }

  @Test
  void getReserves_shouldReturnEmptyResponse() throws Exception {
    doGet(mockMvc, RESERVES_EMPTY_RESPONSE_URL)
      .andExpect(status().isOk())
      .andExpect(jsonPath("totalRecords", is(0)));
  }

  static class LimitAndOffsetProvider implements ArgumentsProvider {

    String[] allItems = new String[]{
      "Adams Christa A", "Taylor Mike", "Aagard Madgeline", "Kim Ammons"
    };

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
      return Stream.of(
        arguments(0, 4, 4, allItems),
        arguments(4, 5, 0, new String[]{}),
        arguments(3, 3, 1, new String[]{"Kim Ammons"}),
        arguments(1, 2, 2, new String[]{"Taylor Mike", "Aagard Madgeline"}),
        arguments(0, 3, 3,
          new String[]{"Adams Christa A", "Taylor Mike", "Aagard Madgeline"}),
        arguments(5, 4, 0, new String[]{}),
        arguments(0, 100, 4, allItems)
      );
    }
  }

}
