package org.folio.edge.courses.client;

import java.util.Map;
import tools.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = "application/json")
public interface CourseClient {

  @GetExchange("coursereserves/courses")
  JsonNode getCourseByQuery(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("coursereserves/reserves")
  JsonNode getReservesByQuery(@RequestParam Map<String, ?> requestQueryParameters);

  @GetExchange("coursereserves/courselistings/{listing_id}/reserves")
  JsonNode getReservesByInstanceId(@PathVariable("listing_id") String listingId,
    @RequestParam Map<String, ?> requestQueryParameters);
}
