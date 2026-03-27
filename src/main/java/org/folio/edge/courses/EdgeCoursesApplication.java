package org.folio.edge.courses;

import static org.folio.common.utils.tls.FipsChecker.getFipsChecksResultString;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Log4j2
public class EdgeCoursesApplication {

  public static void main(String[] args) {
    log.info(getFipsChecksResultString());
    SpringApplication.run(EdgeCoursesApplication.class, args);
  }

}
