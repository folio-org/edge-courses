package org.folio.edge.courses.config;

import feign.RequestInterceptor;
import feign.okhttp.OkHttpClient;
import org.folio.edgecommonspring.security.SecurityManagerService;
import org.folio.spring.FolioExecutionContext;
import org.springframework.context.annotation.Bean;

public class CourseClientConfig {

  @Bean
  public OkHttpClient feignOkHttpClient(okhttp3.OkHttpClient okHttpClient) {
    return new OkHttpClient(okHttpClient);
  }

  @Bean
  public RequestInterceptor courseClientRequestInterceptor(FolioExecutionContext context,
    SecurityManagerService securityManagerService) {
    return new CourseClientRequestInterceptor(context, securityManagerService);
  }
}
