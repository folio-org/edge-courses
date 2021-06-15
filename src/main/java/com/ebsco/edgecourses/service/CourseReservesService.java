package com.ebsco.edgecourses.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.replace;

import com.ebsco.courses.domain.dto.RequestQueryParameters;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.spring.integration.XOkapiHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Log4j2
public class CourseReservesService {

  private static final String GET_COURSE_BY_QUERY_URL = "/coursereserves/courses";
  private static final String GET_COURSE_BY_ID_URL = "/coursereserves/courselistings/{listing_id}/reserves";
  private static final String QUERY = "query";
  private static final String LIMIT = "limit";
  private static final String OFFSET = "offset";
  private static final String LANG = "lang";
  private static final String EXPAND = "expand";
  private final RestTemplate restTemplate;
  @Value("${okapi_url}")
  private String okapiUrl;

  public String getCourseReservesByQuery(String tenant, String token, RequestQueryParameters requestQueryParameters) {
    String modCourseUrl = getUrlWithQueryParams(join(okapiUrl, GET_COURSE_BY_QUERY_URL), requestQueryParameters);
    HttpEntity<String> requestCourseReservesByQuery = buildEntityWithRequiredHeaders(tenant, token);
    log.debug("Calling getCourseReservesByQuery with url: {}", modCourseUrl);
    ResponseEntity<String> reservesResponse = restTemplate
        .exchange(modCourseUrl, HttpMethod.GET, requestCourseReservesByQuery, String.class);
    log.debug("Received response while getting getCourseReservesByQuery, status: {}",
        reservesResponse::getStatusCode);
    return reservesResponse.getBody();
  }

  public String getCourseReservesByInstanceId(String instanceId, String tenant, String token, RequestQueryParameters requestQueryParameters) {
    String validGetCoursesByIdUrl = replace(GET_COURSE_BY_ID_URL, "{listing_id}", instanceId);
    String modCourseUrlWithId = getUrlWithQueryParams(join(okapiUrl, validGetCoursesByIdUrl), requestQueryParameters);
    HttpEntity<String> requestCourseReservesById = buildEntityWithRequiredHeaders(tenant, token);
    log.debug("Calling getCourseReservesByInstanceId with url: {}", modCourseUrlWithId);
    ResponseEntity<String> reservesResponse = restTemplate
        .exchange(modCourseUrlWithId, HttpMethod.GET, requestCourseReservesById, String.class);
    log.debug("Received response while getting getCourseReservesByInstanceId, status: {}",
        reservesResponse::getStatusCode);
    return reservesResponse.getBody();
  }

  private String getUrlWithQueryParams(String url, RequestQueryParameters requestQueryParameters) {
    MultiValueMap<String, String> notEmptyQueryParams = getNotEmptyQueryParams(requestQueryParameters);
    return CollectionUtils.isEmpty(notEmptyQueryParams)
        ? url
        : UriComponentsBuilder.fromHttpUrl(url)
            .queryParams(notEmptyQueryParams)
            .build().toUriString();
  }

  private MultiValueMap<String, String> getNotEmptyQueryParams(RequestQueryParameters requestQueryParameters) {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    if (isNotEmpty(requestQueryParameters.getQuery())) {
      queryParams.add(QUERY, requestQueryParameters.getQuery());
    }
    if (Objects.nonNull(requestQueryParameters.getLimit())) {
      queryParams.add(LIMIT, String.valueOf(requestQueryParameters.getLimit()));
    }
    if (Objects.nonNull(requestQueryParameters.getOffset())) {
      queryParams.add(OFFSET, String.valueOf(requestQueryParameters.getOffset()));
    }
    if (isNotEmpty(requestQueryParameters.getLang())) {
      queryParams.add(LANG, requestQueryParameters.getLang());
    }
    if (isNotEmpty(requestQueryParameters.getExpand())) {
      queryParams.add(EXPAND, requestQueryParameters.getExpand());
    }
    return queryParams;
  }

  private HttpEntity<String> buildEntityWithRequiredHeaders(String tenant, String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(XOkapiHeaders.TENANT, tenant);
    headers.add(XOkapiHeaders.TOKEN, token);
    return new HttpEntity<>(headers);
  }
}
