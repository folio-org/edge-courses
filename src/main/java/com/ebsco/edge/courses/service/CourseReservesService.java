package com.ebsco.edge.courses.service;

import com.ebsco.courses.domain.dto.RequestQueryParameters;
import com.ebsco.edge.courses.client.CourseClient;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseReservesService {

  private final CourseClient courseClient;
  @Value("${okapi_url}")
  private String okapiUrl;

  public String getCourseReservesByQuery(String tenant, String token, RequestQueryParameters requestQueryParameters) {
    log.debug("Calling getCourseReservesByQuery with query: {}", requestQueryParameters.getQuery());
    ResponseEntity<String> reservesResponse = courseClient
        .getCourseByQuery(URI.create(okapiUrl), tenant, token, requestQueryParameters);
    log.debug("Received response while getting getCourseReservesByQuery, status: {}",
        reservesResponse::getStatusCode);
    return reservesResponse.getBody();
  }

  public String getCourseReservesByInstanceId(String instanceId, String tenant, String token,
      RequestQueryParameters requestQueryParameters) {
    log.debug("Calling getCourseReservesByInstanceId with id: {}", instanceId);
    ResponseEntity<String> reservesResponse = courseClient
        .getCourseById(URI.create(okapiUrl), instanceId, tenant, token, requestQueryParameters);
    log.debug("Received response while getting getCourseReservesByInstanceId, status: {}",
        reservesResponse::getStatusCode);
    return reservesResponse.getBody();
  }

}
