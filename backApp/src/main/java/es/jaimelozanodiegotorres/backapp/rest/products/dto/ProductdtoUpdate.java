package es.jaimelozanodiegotorres.backapp.rest.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos para la actualización del producto
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductdtoUpdate {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @Positive(message = "El precio no puede ser negativo")
    @NotNull(message = "El precio no puede estar vacío")
    private double precio;
    @Schema(description = "Stock del producto" , example = "10")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @NotNull(message = "El stock no puede estar vacío")
    private Integer stock;
    @Builder.Default
    @Schema(description = "¿Es Gluten Free?" , example = "true")
    private boolean gluten = true;
}
