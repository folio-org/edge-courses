package org.folio.edge.courses.config;

import org.folio.edge.courses.client.CourseClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfiguration {

  private final HttpServiceProxyFactory httpServiceProxyFactory;

  public HttpClientConfiguration(HttpServiceProxyFactory httpServiceProxyFactory) {
    this.httpServiceProxyFactory = httpServiceProxyFactory;
  }

  @Bean
  public CourseClient courseClient() {
    return httpServiceProxyFactory.createClient(CourseClient.class);
  }
}

