package es.jaimelozanodiegotorres.backapp.rest.products.filters;

import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFilters extends CommonFilters <Product> {

    @Builder.Default
    private Optional<String> name = Optional.empty();

    @Builder.Default
    private Optional<Integer> stockMax = Optional.empty();

    @Builder.Default
    private Optional<Integer> stockMin = Optional.empty();

    @Builder.Default
    private Optional<Double> priceMax = Optional.empty();

    @Builder.Default
    private Optional<Double> priceMin = Optional.empty();

    @Builder.Default
    private Optional<Boolean> gluten = Optional.empty();

    @Builder.Default
    private Optional<Long> categoryId = Optional.empty();

    @Override
    public Specification<Product> getSpecifications() {

        Specification<Product> specNombre = (root, query, criteriaBuilder) ->
                name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specStockMax = (root, query, criteriaBuilder) ->
                stockMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("stock"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specStockMin = (root, query, criteriaBuilder) ->
                stockMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specPrecioMax = (root, query, criteriaBuilder) ->
                priceMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specPrecioMin = (root, query, criteriaBuilder) ->
                priceMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specGluten = (root, query, criteriaBuilder) ->
                gluten.map(d -> criteriaBuilder.equal(root.get("gluten"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specCategory = (root, query, criteriaBuilder) ->
                categoryId.map(d -> criteriaBuilder.equal(root.get("category").get("id"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        return Specification.where(specNombre)
                .and(specStockMax)
                .and(specStockMin)
                .and(specPrecioMax)
                .and(specPrecioMin)
                .and(specGluten)
                .and(specCategory);
    }
}
