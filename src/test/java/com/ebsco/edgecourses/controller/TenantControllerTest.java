package com.ebsco.edgecourses.controller;

import static com.ebsco.edgecourses.TestConstants.POST_TENANT_REQUEST;
import static com.ebsco.edgecourses.TestConstants.TENANT_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebsco.edgecourses.BaseIntegrationTests;
import com.ebsco.edgecourses.TestUtil;
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