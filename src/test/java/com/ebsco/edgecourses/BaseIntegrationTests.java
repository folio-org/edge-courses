package com.ebsco.edgecourses;

import static com.ebsco.edgecourses.TestUtil.TEST_TENANT;
import static com.ebsco.edgecourses.TestUtil.asString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.folio.spring.integration.XOkapiHeaders;
import org.folio.tenant.domain.dto.TenantAttributes;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Log4j2
@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
@AutoConfigureMockMvc
public abstract class BaseIntegrationTests {

  protected static final String OKAPI_URL = "http://localhost:9130";
  private static final String EDGE_COURSES_MODULE = "edge-courses-1.0.0";
  private static final String TEST_API_KEY = "eyJzIjoiQlBhb2ZORm5jSzY0NzdEdWJ4RGgiLCJ0IjoidGVzdCIsInUiOiJ0ZXN0X2FkbWluIn0=";

  @SneakyThrows
  protected static ResultActions doGet(MockMvc mockMvc, String url, String tenant) {
    return mockMvc.perform(get(url)
        .headers(defaultHeaders(tenant)));
  }

  private static HttpHeaders defaultHeaders(String tenant) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(APPLICATION_JSON);
    httpHeaders.put(XOkapiHeaders.TENANT, List.of(tenant));
    httpHeaders.put(XOkapiHeaders.URL, List.of(OKAPI_URL));
    httpHeaders.put(XOkapiHeaders.AUTHORIZATION, List.of(TEST_API_KEY));
    return httpHeaders;
  }

  protected void setUpTenant(MockMvc mockMvc) throws Exception {
    mockMvc.perform(post("/_/tenant")
        .content(asString(new TenantAttributes().moduleTo(EDGE_COURSES_MODULE)))
        .headers(defaultHeaders(TEST_TENANT))
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}
