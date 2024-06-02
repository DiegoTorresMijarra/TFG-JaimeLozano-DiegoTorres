package es.jaimelozanodiegotorres.backapp.rest.products.filters;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFiltersDto {

    protected static final String REGEXP_SANITIZER = "^[\\d\\p{L}.,;:]*$";


    @Min(value = 0, message = "Page number should not be less than 0")
    private int page = 0;

    @Min(value = 1, message = "Page size should be at least 1")
    @Max(value = 100, message = "Page size should not exceed 100")
    private int size = 10;

    @Pattern(regexp = REGEXP_SANITIZER, message = "El nombre solo puede contener letras y dígitos")
    private String sortBy = "id";

    @Pattern(regexp = "^(asc|desc)$", message = "Direction should be 'asc' or 'desc'")
    private String direction = "asc";

    @Pattern(regexp = REGEXP_SANITIZER, message = "El nombre solo puede contener letras y dígitos")
    private String name;

    @PositiveOrZero(message = "El stock máximo no puede ser negativo")
    private Integer stockMax;

    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    private Integer stockMin;

    @Positive(message = "El precio máximo debe ser positivo")
    @Digits(integer = 5, fraction = 2, message = "El precio máximo solo puede tener 5 dígitos enteros y 2 decimales")
    private Double priceMax;

    @Positive(message = "El precio mínimo debe ser positivo")
    @Digits(integer = 5, fraction = 2, message = "El precio mínimo solo puede tener 5 dígitos enteros y 2 decimales")
    private Double priceMin;

    private Boolean gluten;

    @Positive(message = "El ID de la categoría debe ser positivo")
    private Long categoryId;


    public ProductFilters getProductFilters() {
        return ProductFilters.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(Optional.ofNullable(name))
                .stockMax(Optional.ofNullable(stockMax))
                .stockMin(Optional.ofNullable(stockMin))
                .priceMax(Optional.ofNullable(priceMax))
                .priceMin(Optional.ofNullable(priceMin))
                .gluten(Optional.ofNullable(gluten))
                .categoryId(Optional.ofNullable(categoryId))
                .build();
    }
}
