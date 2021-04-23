package com.ebsco.edgecourses.controller;

import lombok.extern.log4j.Log4j2;
import org.folio.tenant.domain.dto.TenantAttributes;
import org.folio.tenant.rest.resource.TenantApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController("folioTenantController")
@RequestMapping(value = "/_/")
public class TenantController implements TenantApi {

  @Override
  public ResponseEntity<String> postTenant(TenantAttributes tenantAttributes) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
