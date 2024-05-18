package es.jaimelozanodiegotorres.backapp.rest.commons.filters;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public abstract class CommonFilters {

    @Min(value = 0, message = "Page number should not be less than 0")
    private int page = 0;

    @Min(value = 1, message = "Page size should be at least 1")
    @Max(value = 100, message = "Page size should not exceed 100")
    private int size = 10;

//    @Pattern(regexp = "^(id|name|number)$", message = "Sort field should be 'id', 'name', or 'number'")
    private String sort = "id";

    @Pattern(regexp = "^(asc|desc)$", message = "Direction should be 'asc' or 'desc'")
    private String direction = "asc";
}
