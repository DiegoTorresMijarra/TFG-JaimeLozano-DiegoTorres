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

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 2;

    @Builder.Default
    private String sortBy = "id";

    @Builder.Default
    private String direction = "asc";

    public Pageable getPageable() {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    public abstract Specification<M> getSpecifications();
}
