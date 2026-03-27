package org.folio.edge.courses.config;

import lombok.RequiredArgsConstructor;
import org.folio.edge.courses.client.CourseClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfiguration {

  @Qualifier("edgeHttpServiceProxyFactory")
  private final HttpServiceProxyFactory httpServiceProxyFactory;

  @Bean
  public CourseClient courseClient() {
    return httpServiceProxyFactory.createClient(CourseClient.class);
  }
}

