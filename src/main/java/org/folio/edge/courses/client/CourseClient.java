package org.folio.edge.courses.client;

import org.folio.edge.courses.config.CourseClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course", configuration = CourseClientConfig.class)
public interface CourseClient {

  @GetMapping(value = "/coursereserves/courses", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> getCourseByQuery(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/coursereserves/reserves", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> getReservesByQuery(@SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/coursereserves/courselistings/{listing_id}/reserves", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> getReservesByInstanceId(@PathVariable("listing_id") String listingId,
    @SpringQueryMap Object requestQueryParameters);

  @GetMapping(value = "/coursereserves/departments", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<String> getDepartments(@SpringQueryMap Object requestQueryParameters);

}
