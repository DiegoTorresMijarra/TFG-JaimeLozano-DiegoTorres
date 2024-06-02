package es.jaimelozanodiegotorres.backapp.rest.commons.filters;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class CommonFilters<M> {
    protected static final String REGEXP_SANITIZER = "^[\\d\\p{L}.,;:]*$";

    @Min(value = 0, message = "Page number should not be less than 0")
    private int page = 0;

    @Min(value = 1, message = "Page size should be at least 1")
    @Max(value = 100, message = "Page size should not exceed 100")
    private int size = 10;

//    @Pattern(regexp = "^(id|name|number)$", message = "Sort field should be 'id', 'name', or 'number'")
    @Pattern(regexp = REGEXP_SANITIZER, message = "Sort field should only have letters and digits")
    private String sortBy = "id";

    @Pattern(regexp = "^(asc|desc)$", message = "Direction should be 'asc' or 'desc'")
    private String direction = "asc";

    public Pageable getPageable() {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    public abstract Specification<M> getSpecifications();
}
