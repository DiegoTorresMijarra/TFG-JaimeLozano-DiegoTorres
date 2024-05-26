package es.jaimelozanodiegotorres.backapp.rest.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Objeto de transferencia de datos para la creación del producto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveDto {
    @Schema(description = "Nombre del producto", example = "Coca Cola")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Length(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @Schema(description = "Precio del producto", example = "12.0")
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    @Digits(integer = 5, fraction = 2, message = "El precio del producto solo puede tener 5 digitos enteros y 2 decimales")
    private double price;

    @Schema(description = "Stock del producto", example = "10")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @NotNull(message = "El stock no puede estar vacío")
    private Integer stock;

    @Builder.Default
    @Schema(description = "Gluten del producto" , example = "true")
    private boolean gluten = true;

    @Schema(description = "El id de la categoria del producto",example = "1")
    @Builder.Default
    @PositiveOrZero(message = "El id de la categoria debe ser mayor que 0")
    private long categoryId = 1; //es la cat por defecto (sin categoria)
}
