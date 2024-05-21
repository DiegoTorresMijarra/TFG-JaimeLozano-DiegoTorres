package es.jaimelozanodiegotorres.backapp.rest.products.filters;

import es.jaimelozanodiegotorres.backapp.rest.commons.filters.CommonFilters;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilters extends CommonFilters <Product> {

    //todo: revisar validaciones. No recoge los datos de filtrado pasados, son null siempre

  //  @Pattern(regexp = REGEXP_SANITIZER, message = "nombre field should only have letters and digits")
    @Builder.Default
    private Optional<String> nombre = Optional.empty();

    @Builder.Default
  //  @PositiveOrZero(message = "El stock no puede ser negativo")
    private Optional<Integer> stockMax = Optional.empty();

    @Builder.Default
  //  @PositiveOrZero(message = "El stock no puede ser negativo")
    private Optional<Integer> stockMin = Optional.empty();

    @Builder.Default
  //  @Positive(message = "El precio no puede ser negativo")
  //  @Digits(integer = 5, fraction = 2, message = "El precio del producto solo puede tener 5 digitos enteros y 2 decimales")
    private Optional<Double> precioMax = Optional.empty();

    @Builder.Default
  //  @Positive(message = "El precio no puede ser negativo")
  //  @Digits(integer = 5, fraction = 2, message = "El precio del producto solo puede tener 5 digitos enteros y 2 decimales")
    private Optional<Double> precioMin = Optional.empty();

    @Builder.Default
    private Optional<Boolean> gluten = Optional.empty();

    @Override
    public Specification<Product> getSpecifications() {
        Specification<Product> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(m -> criteriaBuilder.equal(root.get("nombre"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specStockMax = (root, query, criteriaBuilder) ->
                stockMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("stock"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specStockMin = (root, query, criteriaBuilder) ->
                stockMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specPrecioMax = (root, query, criteriaBuilder) ->
                precioMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specPrecioMin = (root, query, criteriaBuilder) ->
                precioMin.map(p -> criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Product> specGluten = (root, query, criteriaBuilder) ->
                gluten.map(d -> criteriaBuilder.equal(root.get("gluten"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        return Specification.where(specNombre)
                .and(specStockMax)
                .and(specStockMin)
                .and(specPrecioMax)
                .and(specPrecioMin)
                .and(specGluten);
    }
}
