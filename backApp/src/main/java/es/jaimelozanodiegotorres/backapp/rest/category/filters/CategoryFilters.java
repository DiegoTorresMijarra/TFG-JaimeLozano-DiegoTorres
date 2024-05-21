package es.jaimelozanodiegotorres.backapp.rest.category.filters;

import es.jaimelozanodiegotorres.backapp.rest.category.models.Category;
import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFilters extends CommonFilters<Category> {

    @Builder.Default
    private Optional<String> name = Optional.empty();

    @Override
    public Specification<Category> getSpecifications() {
        //Criterio por nombre
        Specification<Category> specName=(root, query, criteriaBuilder)->
                name.map(d->criteriaBuilder.equal(root.get("name"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        return Specification.where(specName);
    }
}
