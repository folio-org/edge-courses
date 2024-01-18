package org.folio.edge.courses.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class JsonConverterTest {

  private static final String JSON_BODY = "{\"field\":\"value\"}";
  private static final String WRONG_JSON_BODY = "{\"field\":value}";
  private static final String FIELD_VALUE = "value";

  @Spy
  private final ObjectMapper objectMapper = new ObjectMapper();
  @InjectMocks
  private JsonConverter jsonConverter;

  @Test
  void getObjectFromJson_shouldReadFromStringJson() {
    JsonSample jsonSample = jsonConverter.getObjectFromJson(JSON_BODY, JsonSample.class);
    assertEquals(FIELD_VALUE, jsonSample.getField());
  }

  @Test
  void getObjectFromJson_shouldThrowException_whenJsonBodyIsWrong() {
    assertThatThrownBy(() -> jsonConverter.getObjectFromJson(WRONG_JSON_BODY, JsonSample.class))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("500 INTERNAL_SERVER_ERROR");
  }

}
