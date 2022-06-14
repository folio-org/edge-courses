package com.ebsco.edge.courses.client;

import static org.folio.spring.integration.XOkapiHeaders.TENANT;
import static org.folio.spring.integration.XOkapiHeaders.TOKEN;

import com.ebsco.edge.courses.config.CourseClientConfig;
import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "course", configuration = CourseClientConfig.class)
public interface CourseClient {

  @GetMapping(value = "/coursereserves/courses", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> getCourseByQuery(URI okapiURI, @RequestHeader(TENANT) String tenant,
      @RequestHeader(TOKEN) String token, @SpringQueryMap(encoded = true) Object requestQueryParameters);

  @GetMapping(value = "/coursereserves/courselistings/{listing_id}/reserves", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> getCourseById(URI okapiURI, @PathVariable("listing_id") String listingId,
      @RequestHeader(TENANT) String tenant, @RequestHeader(TOKEN) String token,
      @SpringQueryMap(encoded = true) Object requestQueryParameters);
}
