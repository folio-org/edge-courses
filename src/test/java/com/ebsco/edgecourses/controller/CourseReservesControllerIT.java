package com.ebsco.edgecourses.controller;

import static com.ebsco.edgecourses.TestUtil.EMPTY_RESERVES_RESPONSE_PATH;
import static com.ebsco.edgecourses.TestUtil.RESERVES_RESPONSE_PATH;
import static com.ebsco.edgecourses.TestUtil.TEST_TENANT;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebsco.edgecourses.BaseIntegrationTests;
import com.ebsco.edgecourses.TestUtil;
import com.ebsco.edgecourses.service.CourseReservesService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.folio.edgecommonspring.client.AuthnClient;
import org.folio.edgecommonspring.domain.entity.ConnectionSystemParameters;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

class CourseReservesControllerIT extends BaseIntegrationTests {

  public static final String RESERVES_URL_WITH_QUERY = "/courses/courses?query=id=67227d94-7333-4d22-98a0-718b49d36595";
  public static final String COURSE_RESERVE_UUID = "67227d94-7333-4d22-98a0-718b49d36595";
  public static final String RESERVES_URL_BY_ID = "/courses/courselistings/67227d94-7333-4d22-98a0-718b49d36595/reserves";
  private static final String MOCK_TOKEN = "eyJhbGciOiJIUzI1NiJ9eyJzdWIiOiJ0ZXN0X2FkbWluIiwidXNlcl9pZCI6ImQyNjUwOGJlLTJmMGItNTUyMC1iZTNkLWQwYjRkOWNkNmY2ZSIsImlhdCI6MTYxNjQ4NDc5NCwidGVuYW50IjoidGVzdCJ9VRYeA0s1O14hAXoTG34EAl80";
  private static final String LOGIN_RESPONSE_BODY = "{\r\n    \"username\": \"test_admin\",\r\n    \"password\": \"admin\"\r\n}";
  private MockRestServiceServer mockRestServiceServer;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RestOperations restOperations;

  @MockBean
  private AuthnClient authnClient;

  @Autowired
  private CourseReservesService courseReservesService;

  @BeforeEach
  void before() throws Exception {
    setUpTenant(mockMvc);
    mockRestServiceServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    Mockito.when(authnClient.getApiKey(any(ConnectionSystemParameters.class), anyString()))
        .thenReturn(getLoginResponse());
    ReflectionTestUtils
        .setField(courseReservesService, "okapiUrl", "http://localhost:9130");
  }

  @Test
  void getCourses_shouldReturnCourses() throws Exception {
    expectGetCoursesByQueryResponseDependsOnResource(RESERVES_RESPONSE_PATH);
    doGet(mockMvc, RESERVES_URL_WITH_QUERY, TEST_TENANT)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(1)))
        .andExpect(jsonPath("reserves[0].id", is(COURSE_RESERVE_UUID)));
  }

  @Test
  void getCoursesByInstanceId_shouldReturnCourses() throws Exception {
    expectGetCoursesByIdResponseDependsOnResource(RESERVES_RESPONSE_PATH);
    doGet(mockMvc, RESERVES_URL_BY_ID, TEST_TENANT)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(1)))
        .andExpect(jsonPath("reserves[0].id", is(COURSE_RESERVE_UUID)));
  }

  @Test
  void getCourses_shouldReturnEmpty_whenCoursesResponseIsEmpty() throws Exception {
    expectGetCoursesByQueryResponseDependsOnResource(EMPTY_RESERVES_RESPONSE_PATH);
    doGet(mockMvc, RESERVES_URL_WITH_QUERY, TEST_TENANT)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void getCoursesByInstanceId_shouldReturnEmpty_whenCoursesByIdResponseIsEmpty() throws Exception {
    expectGetCoursesByIdResponseDependsOnResource(EMPTY_RESERVES_RESPONSE_PATH);
    doGet(mockMvc, RESERVES_URL_BY_ID, TEST_TENANT)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  private void expectGetCoursesByQueryResponseDependsOnResource(String resource) {
    String content = TestUtil.readFileContentFromResources(resource);
    mockRestServiceServer.expect(
        requestTo(OKAPI_URL + "/coursereserves/reserves?query=id%3D" + COURSE_RESERVE_UUID))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(content, APPLICATION_JSON));
  }

  private void expectGetCoursesByIdResponseDependsOnResource(String resource) {
    String content = TestUtil.readFileContentFromResources(resource);
    mockRestServiceServer.expect(
        requestTo(OKAPI_URL + "/coursereserves/courselistings/" + COURSE_RESERVE_UUID + "/reserves"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(content, APPLICATION_JSON));
  }

  private ResponseEntity<String> getLoginResponse() {
    URI uri = null;
    try {
      uri = new URI("http://localhost:9130/login");
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return ResponseEntity.created(Objects.requireNonNull(uri))
        .header(XOkapiHeaders.TOKEN, MOCK_TOKEN)
        .body(LOGIN_RESPONSE_BODY);
  }

}
