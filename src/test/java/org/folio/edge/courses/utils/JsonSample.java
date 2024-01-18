package org.folio.edge.courses.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonSample {

    String field;

    @JsonCreator
    public JsonSample(@JsonProperty("field") String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

}
