package org.folio.edge.courses.model.dto;

import java.util.stream.Stream;

public enum SortDirection {
  ASCENDING("sort.ascending"),
  DESCENDING("sort.descending");

  private final String value;

  SortDirection(String value) {
    this.value = value;
  }

  public static SortDirection fromString(String value) {
    return Stream.of(values())
      .filter(sortDirection -> sortDirection.value.equalsIgnoreCase(value))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Invalid sort direction: " + value));
  }

}
