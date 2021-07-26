package com.ebsco.edgecourses;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

@Log4j2
public class TestUtil {

  public static final String RESERVES_RESPONSE_PATH = "__files/reserves/reserves_response.json";
  public static final String COURSES_RESPONSE_PATH = "__files/courses/courses_response.json";
  public static final String TEST_TENANT = "test";
  public static final String RESERVES = "reserves";
  public static final String COURSES = "courses";

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public static String readFileContentFromResources(String path) {
    try {
      ClassLoader classLoader = TestUtil.class.getClassLoader();
      URL url = classLoader.getResource(path);
      return IOUtils.toString(url, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new IllegalStateException(e);
    }
  }

}
