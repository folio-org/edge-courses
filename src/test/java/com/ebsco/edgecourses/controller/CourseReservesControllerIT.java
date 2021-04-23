package com.ebsco.edgecourses.controller;

import static com.ebsco.edgecourses.TestUtil.TEST_TENANT;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebsco.edgecourses.BaseIntegrationTests;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

class CourseReservesControllerIT extends BaseIntegrationTests {

  public static final String RESERVES_URL_WITH_QUERY = "/courses/courses?query=id=67227d94-7333-4d22-98a0-718b49d36595";
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

  @BeforeEach
  void before() throws Exception {
    setUpTenant(mockMvc);
    mockRestServiceServer = MockRestServiceServer.createServer((RestTemplate) restOperations);
    Mockito.when(authnClient.getApiKey(any(ConnectionSystemParameters.class), anyString()))
        .thenReturn(getLoginResponse());
  }

  @Test
  void getCourses_shouldReturnCourses() throws Exception {
    doGet(mockMvc, RESERVES_URL_WITH_QUERY, TEST_TENANT)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(1)))
        .andExpect(jsonPath("reserves[0].id", is("67227d94-7333-4d22-98a0-718b49d36595")));
  }

  @Test
  void getCoursesByInstanceId_shouldReturnCourses() throws Exception {
    doGet(mockMvc, RESERVES_URL_BY_ID, TEST_TENANT)
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(1)))
        .andExpect(jsonPath("reserves[0].id", is("67227d94-7333-4d22-98a0-718b49d36595")));
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
