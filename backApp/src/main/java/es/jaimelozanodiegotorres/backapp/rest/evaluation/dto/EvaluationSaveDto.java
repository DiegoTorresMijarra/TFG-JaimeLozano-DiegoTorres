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
public class EvaluationSaveDto {
    @PositiveOrZero(message = "La valoracion no puede ser negativa")
    @NotNull(message = "La valoracion no puede estar vacía")
    @Max(value = 5)
    @Schema(description = "Valoracion del producto", example = "1")
    private Integer valoracion;
    @Schema(description = "Id del producto", example = "1")
    @NotNull(message="El id no puede estar vacío")
    @Positive(message = "El id no puede ser negativo o 0")
    private Long productId;
}
