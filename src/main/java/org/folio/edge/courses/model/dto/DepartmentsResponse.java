package org.folio.edge.courses.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.folio.courses.domain.dto.Department;

public record DepartmentsResponse(
    @JsonProperty("departments") List<Department> departments,
    @JsonProperty("totalRecords") int totalRecords
) {

}