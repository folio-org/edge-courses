package org.folio.edge.courses.controller;

import static org.folio.edge.courses.TestConstants.POST_TENANT_REQUEST;
import static org.folio.edge.courses.TestConstants.TENANT_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.courses.BaseIntegrationTests;
import org.folio.edge.courses.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

class TenantControllerTest extends BaseIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void postTenant_shouldReturnHttpStatusOk() throws Exception {
    String requestBody = TestUtil.readFileContentFromResources(POST_TENANT_REQUEST);

    doPost(mockMvc, TENANT_URL, requestBody)
      .andExpect(status().isNoContent());
  }
}
