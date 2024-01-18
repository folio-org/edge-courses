package org.folio.edge.courses.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@Service
@RequiredArgsConstructor
public class JsonConverter {

  private final ObjectMapper objectMapper;

  /**
   * Read json object from the json value by type
   * @param value json value
   * @param type type reference to convert json to object
   * @param <T> type of the object for conversion
   * @return converted json object from json value
   */
  public <T> T getObjectFromJson(String value, Class<T> type) {
    try {
      return objectMapper.readValue(value, type);
    } catch (IOException e) {
      String errorMessage = String.format("Failed to get object from json value: %s", value);
      log.error(errorMessage, e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }
  }

}
