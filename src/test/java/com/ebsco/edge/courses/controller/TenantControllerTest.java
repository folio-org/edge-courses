package com.ebsco.edge.courses.controller;

import static com.ebsco.edge.courses.TestConstants.POST_TENANT_REQUEST;
import static com.ebsco.edge.courses.TestConstants.TENANT_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebsco.edge.courses.TestConstants;
import com.ebsco.edge.courses.TestUtil;
import com.ebsco.edge.courses.BaseIntegrationTests;
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