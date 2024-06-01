package es.jaimelozanodiegotorres.backapp.rest.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {
    private static final String REGEXP_SANITIZER = "^[\\d\\p{L}\\s,.;:]*$";

    @Schema(description = "Valoracion del producto", example = "1")
    @PositiveOrZero(message = "La valoracion no puede ser negativa")
    @NotNull(message = "La valoracion no puede estar vacía")
    @Max(value = 5)
    private Integer value;

    @Schema(description = "Valoracion del producto", example = "1")
    @Pattern(regexp = REGEXP_SANITIZER, message = "La valoracion del producto debe tener solo letras y numeros")
    private String comment;

    @Schema(description = "Id del producto", example = "1")
    @NotNull(message="El id del producto no puede estar vacío")
    @Positive(message = "El id no puede ser negativo o 0")
    private long productId;
}
