package com.ebsco.edgecourses.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.folio.spring.integration.XOkapiHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseReservesService {

  private static final String GET_COURSE_BY_QUERY_URL = "/coursereserves/reserves";
  private static final String GET_COURSE_BY_ID_URL = "/coursereserves/courselistings/{listing_id}/reserves";
  private static final String QUERY_URL_PART = "?query=";
  private final RestTemplate restTemplate;
  @Value("${okapi_url}")
  private String okapiUrl;

  public String getCourseReservesByQuery(String query, String tenant, String token) {
    String modCourseUrl = buildProperUrlDependsOnQuery(query);
    HttpEntity<String> requestCourseReservesByQuery = buildEntityWithRequiredHeaders(tenant, token);
    ResponseEntity<String> reservesResponse = restTemplate
        .exchange(modCourseUrl, HttpMethod.GET, requestCourseReservesByQuery, String.class);
    return reservesResponse.getBody();
  }

  public String getCourseReservesByInstanceId(String instanceId, String tenant, String token) {
    String validGetCoursesByIdUrl = StringUtils.replace(GET_COURSE_BY_ID_URL, "{listing_id}", instanceId);
    String modCourseUrlWithId = StringUtils.join(okapiUrl, validGetCoursesByIdUrl);
    HttpEntity<String> requestCourseReservesById = buildEntityWithRequiredHeaders(tenant, token);
    ResponseEntity<String> reservesResponse = restTemplate
        .exchange(modCourseUrlWithId, HttpMethod.GET, requestCourseReservesById, String.class);
    return reservesResponse.getBody();
  }

  private String buildProperUrlDependsOnQuery(String query) {
    return StringUtils.isEmpty(query)
        ? StringUtils.join(okapiUrl, GET_COURSE_BY_QUERY_URL)
        : StringUtils.join(okapiUrl, GET_COURSE_BY_QUERY_URL, QUERY_URL_PART, query);

  }

  private HttpEntity<String> buildEntityWithRequiredHeaders(String tenant, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(XOkapiHeaders.TENANT, tenant);
    headers.add(XOkapiHeaders.TOKEN, token);
    return new HttpEntity<>(headers);
  }
}
